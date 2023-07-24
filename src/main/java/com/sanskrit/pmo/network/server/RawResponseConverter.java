
package com.sanskrit.pmo.network.server;


import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class RawResponseConverter implements Converter {
    GenericCallback listener;

    public RawResponseConverter(GenericCallback callback) {
        this.listener = callback;
    }

    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            InputStream in = body.in();
            String string = fromStream(in);
            in.close();
            this.listener.success(string);
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            this.listener.failure();
            throw new ConversionException(e);
        }
    }

    public TypedOutput toBody(Object object) {
        return null;
    }

    private static String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                return out.toString();
            }
            if (out.length() > 1000000) {
                throw new IOException("Input too long");
            }
            out.append(line);
            out.append("\r\n");
        }
    }
}

