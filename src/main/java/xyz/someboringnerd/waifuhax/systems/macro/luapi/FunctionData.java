package xyz.someboringnerd.waifuhax.systems.macro.luapi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionData {
    String name();
}
