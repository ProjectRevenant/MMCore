package com.gestankbratwurst.core.mmcore.data.redis;

import com.gestankbratwurst.core.mmcore.data.json.GsonProvider;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

public class RedisGsonCodec extends BaseCodec {

  private final GsonEncoder encoder = new GsonEncoder();
  private final GsonDecoder decoder = new GsonDecoder();

  @Override
  public Decoder<Object> getValueDecoder() {
    return this.decoder;
  }

  @Override
  public Encoder getValueEncoder() {
    return this.encoder;
  }

  @Override
  public ClassLoader getClassLoader() {
    if (GsonProvider.class.getClassLoader() != null) {
      return GsonProvider.class.getClassLoader();
    }
    return super.getClassLoader();
  }
}
