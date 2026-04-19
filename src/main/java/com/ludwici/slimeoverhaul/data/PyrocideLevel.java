package com.ludwici.slimeoverhaul.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PyrocideLevel(int level) {
    public static final Codec<PyrocideLevel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("level").forGetter(PyrocideLevel::level)
    ).apply(instance, PyrocideLevel::new));

    public static final StreamCodec<ByteBuf, PyrocideLevel> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PyrocideLevel::level,
            PyrocideLevel::new
    );

    public static final PyrocideLevel DEFAULT = new PyrocideLevel(1);
    public static final PyrocideLevel MIN_LEVEL = new PyrocideLevel(1);
    public static final PyrocideLevel MAX_LEVEL = new PyrocideLevel(4);
}
