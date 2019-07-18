package com.zzx.okhttptest;

public class Contant {

//    public static String BaseUrl = "https://rm.gsjn.gov.cn";

//    public static String BaseUrl = "http://125.75.235.242:80";
//    public static String BaseUrl = "http://125.75.235.242";
    public static String BaseUrl = "http://125.75.235.242:81";

    public static String HomeIndexPageUrl = BaseUrl + "/home/Indexpage/index";
    public static String HomeIndexPageNewsIndexUrl = BaseUrl + "/home/indexpage/news_index";
    public static String HomeIndexPageVideoIndexUrl = BaseUrl + "/home/indexpage/video_index";

    /**
     * 登录
     */
    public static String LoginloginUrl = BaseUrl + "/home/Login/login";
    public static String LoginSendSmsaliUrl = BaseUrl + "/home/Login/send_smsali";
    public static String LoginTownListUrl = BaseUrl + "/home/Login/town_list";
    public static String LoginVillageListUrl = BaseUrl + "/home/Login/village_list";
    public static String LoginMeUrl = BaseUrl + "/home/Login/me";

    /**
     * 三农
     */
    public static String AgricultureTechnologyCategoryUrl = BaseUrl + "/home/three/three_jishu_category";
    public static String AgricultureTechnologyListUrl = BaseUrl + "/home/three/three_jishu_lists";
    public static String AgricultureTechnologyDetailUrl = BaseUrl + "/home/three/three_jishu_detail";
    public static String AgriculturePolicyCategoryUrl = BaseUrl + "/home/three/three_zhengce_category";
    public static String AgriculturePolicyDetailUrl = BaseUrl + "/home/three/three_zhengce_detail";
    public static String AgriculturePolicyListsUrl = BaseUrl + "/home/three/three_zhengce_lists";
    public static String AgricultureCapitalUrl = BaseUrl + "/home/three/capital";

    /**
     * 村务
     */
    //扶贫政策列表
    public static String VillageAffairsPovAllPolicyListUrl = BaseUrl + "/home/Cunwu/cunwu_lists";
    //扶贫政策详情
    public static String VillageAffairsPovAllPolicyDetailUrl = BaseUrl + "/home/Cunwu/cunwu_detail";

    /**
     * 党建
     */
    //首页
    public static String PartyBuildingIndexUrl = BaseUrl + "/home/Party/index";
    //党建讲堂列表
    public static String PartyBuildingClasRoomUrl = BaseUrl + "/home/Party/get_list_data";
    //照片墙列表
    public static String PartyBuildingPhotoWallUrl = BaseUrl + "/home/Party/get_list_data";
    //党务动态页(详情页)
    public static String PartyBuildingDynamicUrl = BaseUrl + "/home/Party/get_content_data";
    //详情
    public static String PartyBuildingClassPhotoDetailUrl = BaseUrl + "/home/Party/info";
    //党建事件
    public static String PartyBuildingEventUrl = BaseUrl + "/home/Party/event";
    //党建 三个带头人
    public static String PartyBuildingLeaderUrl = BaseUrl + "/home/Party/leader";
    //带头人信息详情
    public static String PartyBuildingLeaderInfoUrl = BaseUrl + "/home/Party/leader_detail";


}
