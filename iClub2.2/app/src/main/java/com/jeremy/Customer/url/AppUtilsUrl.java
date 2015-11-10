package com.jeremy.Customer.url;

/**
 * Created by xiaoshengping on 2015/9/8.
 */
public class AppUtilsUrl  {

    public static final String BaseUrl="http://www.iclubapps.com/";
    public  static final String ImageBaseUrl="http://www.iclubapps.com/upload/";
    //艺人头部公告栏
    public static String getArtistHead(){
        return BaseUrl+"getArtistTopPicture.action";

    }
    public static String getArtistList(String area,String sex,String artistCategory,int offset){
        return BaseUrl+"artistFind.action?"+area+sex+artistCategory+"limit=10&"+"offset="+offset;


    }
    //艺人列表
    public static String getArtistListArea(int area,int limit,int offset){
        return BaseUrl+"artistFind.action?"+"area="+area+"&limit="+limit+"&offset="+offset;

    }
    //人才列表
    public static String getTalentList(){

        return BaseUrl+"getPersonResume.action?offset=0&limit=10";

    }
    //登录
    public static String getLoginData(String uid,String pwd){

        return BaseUrl+"login_V2.action?"+"uid="+uid+"&pwd="+pwd;

    }
    //注册
    public static String getRegisterData(){
        //SignUp_V2.action? uid=13800138000&pwd=abc123&vcode=888888
        return BaseUrl+"SignUp_V2_2.action";

    }
    //短信验证
    public static String getVcodeData(String phone){
        //sendVcode.action? phone=13800138000
        return BaseUrl+"sendVcode.action?"+"phone="+phone;

    }
    //忘记密码
    public static String getForgetData(){
        //Reset_V2.action? uid=13800138000&pwd=abc123&vcode=888888
        return BaseUrl+"Reset_V2.action";

    }
    //修改头像editUsericon.action
    public static String getEditUserIcon(){
        return BaseUrl+"editUsericon.action" ;

    }
    //修改昵称editUsername.action
    public static String getEditUserName(){

        return BaseUrl+"editUsername.action" ;

    }

    //保存简历

    public static String getAddResume(){
        //addPersonResume.action
        return BaseUrl+"addPersonResume.action" ;

    }

    //刷新简历
    public static String getRefreshResume(){
        //refreshResume.action
        return BaseUrl+"refreshResume.action" ;

    }
    //刷新招聘列表
    public static String getRefreshMerchant(){
        //refreshJob.action
        return BaseUrl+"refreshJob.action" ;

    }
    //人才--公开/保密简历
    public static String getSaveStateResume(){
        //refreshResume.action
        return BaseUrl+"displayHideResume.action" ;

    }
    //人才--公开/保密简历
    public static String getSaveStateMerchant(){
        //displayHideJob.action
        return BaseUrl+"displayHideJob.action" ;

    }

    //图片介绍
    public static String getAddPicture(){
        //editResumeAddPicture_V2_2.action?
        return BaseUrl+"editResumeAddPicture_V2_2.action" ;

    }

    //视频介绍
    public static String getAddMovie(){
        //editResumeAddMovie_V2_2.action?
        return BaseUrl+"editResumeAddMovie_V2_2.action" ;

    }

    //音乐介绍
    public static String getAddMusic(){
        //editResumeAddMusic_V2_2.action
        return BaseUrl+"editResumeAddMusic_V2_2.action" ;

    }
    //简历列表
    public static String getResumeList(String pid,int offset){
        //getResumeListByUid.action?uid=13800138000
        return BaseUrl+"getResumeListByUid.action?"+"pid="+pid+"&offset="+offset+"&limit=10";

    }
    public static String getResumeLista(String uid){
        //getResumeListByUid.action?uid=13800138000
        return BaseUrl+"getResumeListByUid.action?"+"pid="+uid;

    }
    //公告信息
    public static String getInformationList(String offset,String limit){
        //getMsgListIOS?offset=0&limit=10
        return BaseUrl+"getMsgListIOS?offset="+offset+"&limit="+limit;

    }
    //列表信息通知
    public static String getMessageList(String uid,int offset){
        //getMsgListIOS?offset=0&limit=10
        return BaseUrl+"getApplyListByPerson.action?uid="+uid+"&offset="+offset+"&limit=10";

    }
    //用户--投递消息（商家用户使用）
    public static String getMessageMerchantList(String uid,int offset){
        //getApplyList.action?uid=15088138598&offset=0&limit=10
        return BaseUrl+"getApplyList.action?uid="+uid+"&offset="+offset +"&limit=10";

    }


    //修改密码
    public static String getAmendPsw(){
        // changePassword_V2_1.action
        return BaseUrl+"changePassword_V2_1.action";

    }

    //意见反馈
    public static String getAboutIdea(){
        //submitFeedback.action?content=XXX
        return BaseUrl+"submitFeedback.action";

    }
    //编辑简历
    public static String getCompileResume(){
        //submitFeedback.action?content=XXX
        return BaseUrl+"editResume_V2_2.action";

    }

    //删除简历
    public static String getDeleteResume(){
        //deleteResume.action
        return BaseUrl+"deleteResume.action";

    }
    //预览人才简历
    public static String getPreviewResume(){
        //deleteResume.action
        return BaseUrl+"getResumePreview.action";

    }

    //删除简历
    public static String getResumeBackground(){
        //editResumeUserbg_V2_2.action
        return BaseUrl+"editResumeUserbg_V2_2.action";

    }
    //招聘历史
    public static String getRecruitmentHistoryList(String uid,int offset){
        //getHistoryJobList.action
        return BaseUrl+"getHistoryJobList.action?"+"uid="+uid+"&offset="+offset+"&limit=10";

    }
    //招聘历史
    public static String getRecruitmentHistoryListOne(){
        //getHistoryJobList.action
        return BaseUrl+"getHistoryJobList.action";

    }

    //添加招聘
    public static String getAddJod(){
        // addJob.action
        return BaseUrl+"addJob.action";

    }

    //修改招聘
    public static String getEditJod(){
        // editJob.action
        return BaseUrl+"editJob.action";

    }
    //商家修改信息
    public static String getUpdateMerchant(){
        //changeBeInfo.action
        return BaseUrl+"changeBeInfo.action";

    }
    //获取商家信息
    public static String getAcquireMerchant(){
        //getBeInfo_V2_2.action
        return BaseUrl+"getBeInfo_V2_2.action";

    }

    //删除招聘
    public static String getDeleteJob(){
        // deleteJob.action
        return BaseUrl+"deleteJob.action";

    }
    //招聘商家招聘职位详情
    public static String getJodParticular(){
        // addJob.action
        return BaseUrl+"getJobPreview.action";

    }
    //删除图片
    public static String getDeletePicture(){
        // editResumeDeletePicture_V2_2.action
        return BaseUrl+"editResumeDeletePicture_V2_2.action";

    }
    //删除音乐
    public static String getDeleteMusic(){
        // editResumeDeleteMusic_V2_2.action
        return BaseUrl+"editResumeDeleteMusic_V2_2.action";

    }

    //删除视频
    public static String getDeleteVideo(){
        // editResumeDeleteMovie_V2_2.action?resumeid=13&id=33
        return BaseUrl+"editResumeDeleteMovie_V2_2.action";

    }
    //邀约通知列表
   /* public static String getInviteMessageList(){
        // inviteList.action
        return BaseUrl+"inviteListByPerson.action";

    }*/
    public static String getInviteMessage(String uid,String value,int offset){
        // inviteList.action
        return BaseUrl+"inviteListByPerson.action?"+"uid="+uid+"&value="+value+"&offset="+offset+"&limit=10";

    }
    //接受或者拒绝邀约
    public static String getAdoptAndRefuse(){
        // modifyInviteByPerson.action
        return BaseUrl+"modifyInviteByPerson.action";

    }
    //商家--修改邀约状态
    public static String getModificationMerchant(){
        //modifyInviteByBusiness.action?
        return BaseUrl+"modifyInviteByBusiness.action";

    }
    //人才--修改邀约状态
    public static String getModificationResume(){
        // modifyInviteByPerson.action
        return BaseUrl+"modifyInviteByPerson.action";

    }


    //信誉值
    public static String getReputationData(){
        // grade.action
        return BaseUrl+"grade.action";

    }
    //人才评论商家
    public static String getCommentCommit(){
        // addCommentByPerson.action
        return BaseUrl+"addCommentByPerson.action";

    }
    //人才给商家评分
    public static String getCommentGrade(){
        // modifyGrade.action
        return BaseUrl+"modifyGrade.action";

    }
    //商家评论人才
    public static String getCommentResume(){
        // addCommentByPerson.action
        return BaseUrl+"addCommentByBePerson.action";

    }



    //添加公司图片
    public static String getAddBePicture(){
        // addBePicture.action
        return BaseUrl+"addBePicture.action";

    }


    //商家--来自人才的评论
    public static String getResumeCommentData(String personid,int offset){
        // getCommentByBePerson.action
        return BaseUrl+"getCommentByBePerson.action?personid="+personid+"&offset="+offset+"&limit=10";

    }

    public static String getCommentByPersonUid(String uid,int offset){
        // getCommentByBePerson.action
        return BaseUrl+"getCommentByPersonUid.action?uid="+uid+"&offset="+offset+"&limit=10";

    }
    //商家邀约列表
    public static String getMerchantInvite(String uid,String value,int offset){
        // inviteListByBusiness.action
        return BaseUrl+"inviteListByBusiness.action?"+"uid="+uid+"&value="+value+"&offset="+offset+"&limit=10";

    }
    //微博登陆
    public static String LoginWeiBo(){
        // login_weibo.action
        return BaseUrl+"login_weibo.action";

    }
    public static String getBoundData(){
        // login_weibo.action
        return BaseUrl+"bind_mobile.action";

    }

    //角色用户
    public static String getAddRoleTalents(){
        // deleteJob.action
        return BaseUrl+"role.action";

    }
    public static String getRecruitmentImage(){
        return ImageBaseUrl+"getJobTopPicture.action";
    }

    public static String getRecruitmentList(/*int jobCategory ,int offset , int limit*/){//jobCategory(��Ƹ����),offset(�Ӷ�������ʼ) ,limit(һ��ȡ������)
//        return BaseUrl+"getJobList_V2_1.action?jobCategory="+jobCategory+"&offset="+offset+"&limit="+limit;
        return BaseUrl+"getJobList_V2_1.action?jobCategory=0&offset=0&limit=5";
    }
    public static String getRecruitmentList(int cityid ,int jobCategory,int offset){//jobCategory(��Ƹ����),offset(�Ӷ�������ʼ) ,limit(һ��ȡ������)
        return BaseUrl+"getJobList_V2_2.action?cityid="+cityid+"&jobCategory="+jobCategory+"&offset="+offset+"&limit=10";
//        return BaseUrl+"getJobList_V2_1.action?jobCategory=0&offset=0&limit=5";
    }

    public static String getArtistViewCount(int id){
        return BaseUrl+"artistViewCount.action?artistid="+id;
    }

    public static String getArtistSeekList(){

        return BaseUrl+"artistFindByName.action?";
    }



    public static String getTalentList(int cityid , int jobcategory,int offset){

        return BaseUrl+"getPersonResume.action?cityid="+cityid+"&jobCategory="+jobcategory+"&offset="+offset+"&limit=10";

    }



    public static String getRecruitmentList(int cityid ,int jobCategory ,int offset,String abc /*, int limit*/){//jobCategory(招聘分类),offset(从多少条开始) ,limit(一共取多少条)
//        return BaseUrl+"getJobList_V2_1.action?jobCategory="+jobCategory+"&offset="+offset+"&limit="+limit;
        return BaseUrl+"getJobList_V2_1.action?";//cityid="+cityid+"&jobCategory="+jobCategory+"&keyWord="+abc+"&offset="+offset+"&limit=10";
    }

    public static String getViewCount(int jobid){
        return BaseUrl+"jobViewCount.action?jobid="+jobid;
    }

    public static String getSend(int jobid ,int resumeid){
        return BaseUrl+"applyjob.action?jobid="+jobid+"&resumeid="+resumeid;
    }
    public static String getTalenViewCount(int id){
        return BaseUrl+"resumeViewCount.action?resumeid="+id;
    }
    public static String getLoginQQ(String tokenvalue){
        return BaseUrl+"login_qq.action?token="+tokenvalue;
    }
    public static String getLoginWeibo(String tokenvalue){
        return BaseUrl+"login_weibo.action?weibouid="+tokenvalue;
    }
    public static String getRoute(String personid,String route){
        return BaseUrl+"getTripByMonth.action?personid="+personid+"&yearAndMonth="+route;
    }
    public static String getTalentsRoute(String personid,String route){
        return BaseUrl+"trip.action?uid="+personid+"&yearAndMonth="+route;
    }
    public static String getInvite(String tripTime,String uid,int resumeid){
//        return BaseUrl+"invite.action?tripTime=2015-06-30&uid=13800138000&resumeid=19";
        return BaseUrl+"invite.action?tripTime="+tripTime+"&uid="+uid+"&resumeid="+resumeid;
    }
    public static String getReputationValue(int personid){
        return BaseUrl+"grade.action?personid="+personid;
    }
    public static String getReputationValue(String personid){
        return BaseUrl+"grade.action?personid="+personid;
    }
    public static String getComment(int personid , String Url,int offset){
        return BaseUrl+Url+personid+"&offset="+offset+"&limit=10";
    }
    public static String getComment(String uid , String Url){
        return BaseUrl+Url+uid+"&offset=0&limit=15";
    }
    public static String getModificationRoute(String uid,String tripValues , String yearAndMonth){
        return BaseUrl+"editTrip.action?uid="+uid+"&tripValues="+tripValues+"&yearAndMonth="+yearAndMonth;
    }
    public static String getTalent(int cityid ,int jobCategory,int offset){
        return BaseUrl+"getPersonResume.action?cityid="+cityid+"&jobCategory="+jobCategory+"&offset="+offset+"&limit=10";
    }
    public static String getActivity(){
        return BaseUrl+"activitieList?limit=2&offset=0";
    }
    public static String getActivity(int offset){
        return BaseUrl+"activitieList?limit=10&offset="+offset;
    }
    public static String getRecommendTheVirtuousAndAble(){
        return BaseUrl+"getListByRecommend.action?offset=0&limit=6";
    }
    public static String getRecommendTheVirtuousAndAble(int offset){
        return BaseUrl+"getListByRecommend.action?offset="+offset+"&limit=10";
    }
    public static String getRecommendedWork(){
        return BaseUrl+"getJobList_V2_2_By_Recommend.action?offset=0&limit=5";
    }
    public static String getRecommendedWork(int offset){
        return BaseUrl+"getJobList_V2_2_By_Recommend.action?offset="+offset+"&limit=10";
    }


}
