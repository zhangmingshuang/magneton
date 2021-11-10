package org.magneton.test;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh
 * @since 2021/9/8
 */
class ChaosTestTest {

	@Test
	void test() {
		WarehouseSearchRequestDTO dto = ChaosTest.createExcepted(WarehouseSearchRequestDTO.class);
		System.out.println(dto);
	}

	@Setter
	@Getter
	@ToString(callSuper = true)
	@Accessors(chain = true)
	public static class WarehouseSearchRequestDTO extends PageBaseRequestDTO implements Serializable {

		/**
		 * 店铺id
		 */
		@Nullable
		private String shopIds;

	}

	@Setter
	@Getter
	@ToString
	@Accessors(chain = true)
	public static class PageBaseRequestDTO extends BaseRequestDTO implements Serializable {

		private static final long serialVersionUID = 1L;

		/**
		 * 页码(默认第1页)
		 */
		@Range(min = 1, max = 1000000, message = "页码有效范围1~1000000")
		@NotNull(message = "页码不能为空")
		private Integer pageNo = 1;

		/**
		 * 每页记录数(默认10条)
		 */
		@Range(min = 1, max = 200, message = "每页记录数有效范围1~200")
		@NotNull(message = "每页记录数不能为空")
		private Integer pageSize = 10;

		/**
		 * 是否返回总记录数 true:返回 false:不返回(默认不返回)
		 */
		@Nullable
		private Boolean isReturnCount = false;

		/**
		 * 获取分页开始ID
		 * @return
		 */
		public Long getPageStart() {
			try {
				if (this.pageNo != null && this.pageNo > 0) {
					return Long.valueOf((this.pageNo - 1) * this.pageSize);
				}
			}
			catch (Exception e) {
				throw new RuntimeException("PageToolUtil : 类型转化错误");
			}
			// 若pageNo为空或者小于1则返回 -1
			return -1L;
		}

	}

	@Data
	public static class BaseRequestDTO implements Serializable {

		/**
		 * 应用key
		 */
		private String appKey;

		/**
		 * 系统当前时间(毫秒数)
		 */
		private Long nonce;

		/**
		 * 签名
		 */
		private String sign;

		/**
		 * 接口令牌
		 */
		private String accessToken;

		/**
		 * 集团Id
		 */
		private Long groupId;

		/**
		 * 集团Id
		 */
		private Long shopId;

	}

}