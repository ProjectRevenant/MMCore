package com.gestankbratwurst.core.mmcore.data.redis;

import com.gestankbratwurst.core.mmcore.MMCore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.redisson.client.protocol.Encoder;

@RequiredArgsConstructor
public class GsonEncoder implements Encoder {

  @Override
  public ByteBuf encode(Object in) throws IOException {
    ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
    try(ByteBufOutputStream os = new ByteBufOutputStream(out)) {
      os.writeUTF(MMCore.getGsonProvider().toJson(in));
      os.writeUTF(in.getClass().getName());
      return os.buffer();
    } catch (IOException e) {
      out.release();
      throw e;
    } catch (Exception e) {
      out.release();
      throw new IOException(e);
    }
  }
}
