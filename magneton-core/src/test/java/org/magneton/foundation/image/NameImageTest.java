package org.magneton.foundation.image;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * .
 *
 * @author zhangmsh 22/03/2022
 * @since 2.0.7
 */
class NameImageTest {

	public static void main(String[] args) throws IOException {
		CaptchaPicture build = CaptchaPicture.builder().cornerRadius(0).outputPath(Paths.get("D://name-image"))
				.reverseSub(true).build();
		System.out.println(build.generateImg("张明爽", "test", "zms"));
		System.out.println(build.generateImg("zms", "en-zms"));
	}

}