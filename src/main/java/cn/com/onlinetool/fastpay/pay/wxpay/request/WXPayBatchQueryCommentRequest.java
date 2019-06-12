package cn.com.onlinetool.fastpay.pay.wxpay.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 拉取订单评价数据 请求对象
 * 应用场景：
 *      商户可以通过该接口拉取用户在微信支付交易记录中针对你的支付记录进行的评价内容。
 *      商户可结合商户系统逻辑对该内容数据进行存储、分析、展示、客服回访以及其他使用。
 *      如商户业务对评价内容有依赖，可主动引导用户进入微信支付交易记录进行评价。
 * 注意：
 *      1. 该内容所有权为提供内容的微信用户，商户在使用内容的过程中应遵从用户意愿
 *      2. 一次最多拉取200条评价数据，可根据时间区间分批次拉取
 *      3. 接口只能拉取最近三个月以内的评价数据
 * @date 2019-06-11 11:26
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayBatchQueryCommentRequest extends WXPayBaseRequest {
    /**
     * 开始时间 必填
     * 按用户评论时间批量拉取的起始时间，格式为yyyyMMddHHmmss
     */
    private String beginTime;

    /**
     * 结束时间 必填
     * 按用户评论时间批量拉取的结束时间，格式为yyyyMMddHHmmss
     */
    private String endTime;

    /**
     * 偏移量 必填
     * 指定从某条记录的下一条开始返回记录。
     * 接口调用成功时，会返回本次查询最后一条数据的offset。
     * 商户需要翻页时，应该把本次调用返回的offset 作为下次调用的入参。
     * 注意offset是评论数据在微信支付后台保存的索引，未必是连续的
     */
    private Integer offset;

    /**
     * 条数 非必填
     * 一次拉取的条数, 最大值是200，默认是200
     */
    private Integer limit;
}
