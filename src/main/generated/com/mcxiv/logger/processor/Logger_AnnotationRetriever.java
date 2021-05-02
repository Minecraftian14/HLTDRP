package com.mcxiv.logger.processor;

import com.mcxiv.logger.decorations.Decoration;
import com.mcxiv.logger.decorations.Decorations;
import com.mcxiv.logger.packets.Packet;
import com.mcxiv.logger.util.ByteConsumer;
import com.mcxiv.logger.util.StringsConsumer;

import java.io.OutputStream;

class Logger_AnnotationRetriever extends Logger_StreamDependencyAdder {

    public Logger_AnnotationRetriever(OutputStream stream) {
        super(stream);
    }

    public Logger_AnnotationRetriever() {
        super();
    }

    public Logger_AnnotationRetriever(ByteConsumer consumer) {
        super(consumer);
    }

    public Logger_AnnotationRetriever(StringsConsumer consumer) {
        super(consumer);
    }

    
    
    @Override
    public void prt(String... msg) {
        Decoration decoration = Decorations.get(decorator_name);
        writer.consume(decoration.decorate(msg));
    }

    @Override
    public void prt(Object... obj) {
        Decoration decoration = Decorations.get(decorator_name);
        String[] stf = new String[obj.length];
        for (int i = 0; i < stf.length; i++) stf[i] = obj[i].toString();
        writer.consume(decoration.decorate(stf));
    }

    @Override
    public void raw(String raw) {
        writer.consume(raw);
    }

    @Override
    public StringsConsumer prtf(String... format) {
        Decoration decoration = Decorations.get(decorator_name);
        return msg -> writer.consume(decoration.decorate(msg));
    }

    @Override
    public Packet newPacket() {
        return new OurPacket();
    }

    private class OurPacket extends Packet {

        @Override
        public void prt(String... msg) {
            Decoration decoration = Decorations.get(Decorations.CONSOLE);
            builder.append(decoration.decorate(msg));
        }

        @Override
        public void prt(Object... obj) {
            Decoration decoration = Decorations.get(Decorations.CONSOLE);
            String[] stf = new String[obj.length];
            for (int i = 0; i < stf.length; i++) stf[i] = obj[i].toString();
            builder.append(decoration.decorate(stf));
        }

        @Override
        public void raw(String raw) {
            prtf("").consume(raw);
        }

        @Override
        public StringsConsumer prtf(String... format) {
            Decoration decoration = Decorations.getSpecific(null, Decorations.CONSOLE, format);
            return msg -> builder.append(decoration.decorate(msg));
        }

        @Override
        public void consume() {
            Logger_AnnotationRetriever.this.raw(builder.toString());
//            builder.setLength(0);
        }
    }

}
