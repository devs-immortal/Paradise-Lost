package net.id.paradiselost.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

public class UncheckedWriter implements AutoCloseable{
    private final BufferedWriter writer;
    
    public UncheckedWriter(BufferedWriter writer){
        Objects.requireNonNull(writer, "writer was null");
        this.writer = writer;
    }
    
    @Override
    public void close(){
        try{
            writer.close();
        }catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }
    
    public void write(String string){
        Objects.requireNonNull(string, "string was null");
        try{
            writer.write(string);
        }catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
