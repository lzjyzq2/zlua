package cn.settile.lzjyzq2.zlua;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTest {

    @Test
    void main_c() throws URISyntaxException, IOException {
        Program.main(new String[]{
                "-c",
                Paths.get(Objects.requireNonNull(getClass().getResource("/hello_world.luac")).toURI()).toFile().getAbsolutePath()
        });
    }

    @Test
    void main_r() throws URISyntaxException, IOException {
        Program.main(new String[]{
                "-r", Paths.get(Objects.requireNonNull(getClass().getResource("/sum.out")).toURI()).toFile().getAbsolutePath()
        });
    }

    @Test
    void main_r_t() throws URISyntaxException, IOException {
        Program.main(new String[]{
                "-r", Paths.get(Objects.requireNonNull(getClass().getResource("/test.out")).toURI()).toFile().getAbsolutePath()
        });
    }


    @Test
    void main_r_h() throws URISyntaxException, IOException {
        Program.main(new String[]{
                "-r",
                Paths.get(Objects.requireNonNull(getClass().getResource("/hello_world.luac")).toURI()).toFile().getAbsolutePath()
        });
    }

    @Test
    void main_r_c() throws URISyntaxException, IOException {
        Program.main(new String[]{
                "-r",
                Paths.get(Objects.requireNonNull(getClass().getResource("/counter.out")).toURI()).toFile().getAbsolutePath()
        });
    }
}