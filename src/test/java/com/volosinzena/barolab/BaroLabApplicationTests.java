package com.volosinzena.barolab;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import com.volosinzena.barolab.logging.JunitLogExtension;

@SpringBootTest
@ExtendWith(JunitLogExtension.class)
class BaroLabApplicationTests {

    @Test
    void contextLoads() {
    }

}
