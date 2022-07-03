package cn.settile.lzjyzq2.zlua;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTest {

    @Test
    void main() throws URISyntaxException, IOException {
        Program.main(new String[]{Paths.get(
                Objects.requireNonNull(getClass().getResource("/hello_world.luac")).toURI()).toFile().getAbsolutePath()});
    }
}