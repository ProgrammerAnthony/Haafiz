package edu.com.mvpCommon.umeng.sample;

import com.alibaba.mobileim.fundamental.model.YWIMSmiley;
import com.alibaba.mobileim.ui.chat.widget.YWSmilyMgr;


import java.util.List;

import edu.com.mvpCommon.R;

/**
 * Created by zhaoxu on 2015/10/28.
 * 自定义表情示例
 * 自定义表情，SDK允许开发者替换（新增）现有的表情内容
 *
 * 先约定一下几个概念
 * 1.表情快捷键(shortCuts)，用于在协议中传输的表情符号
 * 2.表情含义(meanings)，表情中文含义，一般用于显示在通知栏中
 * 3.表情资源ID号(smilyRes)，这个好理解，就是Android的资源ID
 *
 * 开发者通过设置以上三个值来进行表情的功能的扩展，以上三个值的个数是一致的
 */
public class SmilyCustomSample {

    /**
     * 该方式初始化表情会默认覆盖SDK自带表情包
     */
    public static void init(){
        addSmilyMeanings();
        addShortCuts();
        addSmily();
    }

    private static void addSmilyMeanings(){
        List<String> list = YWSmilyMgr.getMeanings();
        //在系统默认的表情基础上，追加自己的表情，当然也可以先list.clear()，只使用自己的表情
        list.add("测试表情");
        YWSmilyMgr.setMeanings(list);
    }

    private static void addShortCuts(){
        List<String> list = YWSmilyMgr.getShortCuts();
        //在系统默认的表情基础上，追加自己的表情，当然也可以先list.clear()，只使用自己的表情
        list.add("<>:)-");
        YWSmilyMgr.setShortCuts(list);
    }

    private static void addSmily(){
        List<Integer> list = YWSmilyMgr.getSmilyRes();
        //在系统默认的表情基础上，追加自己的表情，当然也可以先list.clear()，只使用自己的表情
        list.add(R.drawable.__leak_canary_icon);
        YWSmilyMgr.setSmilyRes(list);
    }

    /**
     * 添加新的表情包,该方法添加的表情是图片类型,开发者无需考虑多端解析问题
     * @param smileyResArray
     *          表情资源数组
     * @param horizontalCount
     *          水平展示个数
     * @param verticalCount
     *          垂直展示个数
     * <br>
     * 注:该方法添加的表情属于自定义Image表情,表情会当做图片发送
     */
    public static void addImageSmiley(int[] smileyResArray, int horizontalCount, int verticalCount) {
        YWIMSmiley smiley = new YWIMSmiley(smileyResArray);
        smiley.setHorizontalCount(horizontalCount);
        smiley.setVerticalCount(verticalCount);
        YWSmilyMgr.addNewSmiley(smiley);
    }

    /**
     * 添加新的表情包,该方法会在原有表情包的基础上扩充(没有指定水平和垂直方向上的个数,则使用默认---水平:7 垂直:3)
     * @param smileyResArray
     *      drawable资源id数组
     * @param shortCuts
     *      快捷方式数组
     * @param meanings
     *      含义数组
     * <br>
     * 注:该方法添加的表情属于自定义Emoji表情,填加后的展示需要开发者自己考虑多端解析的实现
     */
    public static void addEmojiSmiley(int[] smileyResArray, String[] shortCuts, String[] meanings) {
        YWIMSmiley smiley = new YWIMSmiley(smileyResArray, shortCuts, meanings);
        YWSmilyMgr.addNewSmiley(smiley);
    }

    /**
     * 添加新的表情包,该方法会在原有表情包的基础上扩充
     * @param smileyResArray
     *      drawable资源id数组
     * @param shortCuts
     *      快捷方式数组
     * @param meanings
     *      含义数组
     * @param horizontalCount
     *      水平方向上的展示个数
     * @param verticalCount
     *      垂直方向上的展示个数
     *
     * <br>
     * 注:该方法添加的表情属于自定义Emoji表情,填加后的展示需要开发者自己考虑多端解析的实现
     */
    public static void addEmojiSmiley(int[] smileyResArray, String[] shortCuts, String[] meanings, int verticalCount, int horizontalCount) {
        YWIMSmiley smiley = new YWIMSmiley(smileyResArray, shortCuts, meanings, horizontalCount, verticalCount);
        YWSmilyMgr.addNewSmiley(smiley);
    }

    /**
     * 导入新的表情包示例代码,开发者请调用{@link #addEmojiSmiley(int[], String[], String[])}
     * 或者{@link #addEmojiSmiley(int[], String[], String[], int, int)}导入
     */
    public static void addNewEmojiSmiley() {
//        YWIMSmiley smiley = new YWIMSmiley(smileResArray, shortCuts, meanings, 5, 2);
//        YWSmilyMgr.addNewSmiley(smiley);
    }

    public static void addNewImageSmiley() {
        addImageSmiley(smileResArray, 6, 3);
    }

    public static int[] smileResArray = new int[] {
            R.drawable.aliwx_s001,
            R.drawable.aliwx_s002,
            R.drawable.aliwx_s003,
            R.drawable.aliwx_s004,
            R.drawable.aliwx_s005,
            R.drawable.aliwx_s006,
            R.drawable.aliwx_s007,
            R.drawable.aliwx_s008,
            R.drawable.aliwx_s009,
            R.drawable.aliwx_s010,
            R.drawable.aliwx_s011,
            R.drawable.aliwx_s012,
            R.drawable.aliwx_s013,
            R.drawable.aliwx_s014,
            R.drawable.aliwx_s015,
            R.drawable.aliwx_s016,
            R.drawable.aliwx_s017,
            R.drawable.aliwx_s018,
            R.drawable.aliwx_s019,
            R.drawable.aliwx_s020};

    private static String[] shortCuts = new String[] { "/:^_^", "/:^$^", "/:Q",
            "/:815", "/:809", "/:^O^", "/:081", "/:087", "/:086", "/:H",
            "/:012", "/:806", "/:b", "/:^x^", "/:814", "/:^W^", "/:080",
            "/:066", "/:807", "/:805"};
    private static String[] meanings = new String[] { "微笑", "害羞", "吐舌头", "偷笑",
            "爱慕", "大笑", "跳舞", "飞吻", "安慰", "抱抱", "加油", "胜利", "强", "亲亲", "花痴",
            "露齿笑", "查找", "呼叫", "算账", "财迷"};
    
}
