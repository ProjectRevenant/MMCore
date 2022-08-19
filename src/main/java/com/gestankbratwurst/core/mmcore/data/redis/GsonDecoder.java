package com.gestankbratwurst.core.mmcore.data.redis;

import com.gestankbratwurst.core.mmcore.MMCore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;

public class GsonDecoder implements Decoder<Object> {

  private final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

  @Override
  public Object decode(ByteBuf buf, State state) throws IOException {
    try (ByteBufInputStream stream = new ByteBufInputStream(buf)) {
      String string = stream.readUTF();
      String type = stream.readUTF();

      Class<?> clazz = this.getClassFromType(type);

      if (clazz == null) {
        return null;
      }

      return MMCore.getGsonProvider().fromJson(string, clazz);
    }
  }

  private Class<?> getClassFromType(String name) {
    Class<?> clazz = this.classMap.get(name);

    if (clazz == null) {
      try {
        clazz = Class.forName(name);
        this.classMap.put(name, clazz);
      } catch (ClassNotFoundException e) {
        return null;
      }
    }

    return clazz;
  }
}
