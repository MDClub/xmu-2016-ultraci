package cn.edu.xmu.ultraci.hotelcheckin.server.service;

import java.util.Map;

import cn.edu.xmu.ultraci.hotelcheckin.server.dto.BaseDTO;

/**
 * 房务类服务接口<br>
 * 
 * @author LuoXin
 *
 */
public interface IRoomService {

	/**
	 * 上报散客信息
	 * 
	 * @param params 参数集
	 * @return 处理结果
	 */
	public BaseDTO newGuest(Map<String, String> params);

	/**
	 * 办理入住手续
	 * 
	 * @param params 参数集
	 * @return 处理结果
	 */
	public BaseDTO checkIn(Map<String, String> params);

	/**
	 * 办理退房手续
	 * 
	 * @param params 参数集
	 * @return 处理结果
	 */
	public BaseDTO checkOut(Map<String, String> params);

	/**
	 * 查询支付结果<br>
	 * 待第三方支付接口调通后实现
	 * 
	 * @param params 参数集
	 * @return 处理结果
	 */
	@Deprecated
	public void pay(Map<String, String> params);
}
