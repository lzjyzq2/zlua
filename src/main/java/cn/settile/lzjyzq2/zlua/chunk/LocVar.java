package cn.settile.lzjyzq2.zlua.chunk;

import cn.settile.lzjyzq2.zlua.util.BinaryChunkUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocVar {

    private String varName;

    private int startPC;

    private int endPC;

    public static LocVar read(ByteBuffer buffer) {
        return LocVar.builder()
                .varName(BinaryChunkUtil.readLuaString(buffer))
                .endPC(buffer.getInt())
                .startPC(buffer.getInt())
                .build();
    }
}
