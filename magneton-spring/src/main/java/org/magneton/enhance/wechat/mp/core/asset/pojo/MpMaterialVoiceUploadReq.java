package org.magneton.enhance.wechat.mp.core.asset.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.File;

/**
 * 素材上传请求.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Setter
@Getter
@ToString
public class MpMaterialVoiceUploadReq {

	/**
	 * 上传的文件.
	 */
	private File file;

	/**
	 * 文件名.
	 * <p>
	 * 如果不设置，那么将使用文件的名称. 如果设置了，但是没有文件名后续，那么将使用文件的当前后缀。
	 *
	 * 如 {@code filename=word.mp3}, 没有设置 {@link #fileName}， 那么将使用 {@code word.mp3} 作为文件名。
	 * 如果设置了 {@link #fileName} 为 {@code hello}, 那么将使用 {@code hello.mp3} 作为文件名。
	 */
	@Nullable
	private String fileName;

}