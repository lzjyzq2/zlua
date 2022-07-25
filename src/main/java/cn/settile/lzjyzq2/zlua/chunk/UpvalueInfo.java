package cn.settile.lzjyzq2.zlua.chunk;

import lombok.*;

import java.nio.ByteBuffer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpvalueInfo {

    private byte instack;

    private byte idx;

    public static UpvalueInfo read(ByteBuffer buffer) {
        return UpvalueInfo.builder()
                .instack(buffer.get())
                .idx(buffer.get())
                .build();
    }
}
