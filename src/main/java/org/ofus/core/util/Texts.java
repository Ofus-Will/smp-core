package org.ofus.core.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class Texts {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    private Texts () {}

    public static Component parse(String text) {
        return LEGACY.deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static void send(Audience audience, String text) {
        audience.sendMessage(parse(text));
    }

    public static void actionBar(Audience audience, String text) {
        audience.sendActionBar(parse(text));
    }

    public static String plain(Component component) {
        return PLAIN.serialize(component);
    }
}