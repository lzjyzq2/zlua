package cn.settile.lzjyzq2.zlua.chunk;

import lombok.*;

import java.nio.ByteBuffer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Upvalue {

    private byte instack;

    private byte idx;

    public static Upvalue read(ByteBuffer buffer) {
        return Upvalue.builder()
                .instack(buffer.get())
                .idx(buffer.get())
                .build();
    }
}
