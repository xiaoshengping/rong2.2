package com.jeremy.Customer.bean;

import com.jeremy.Customer.bean.mine.ResumeMovie;
import com.jeremy.Customer.bean.mine.ResumeMusic;
import com.jeremy.Customer.bean.mine.ResumePicture;

import java.io.Serializable;
import java.util.List;


public class TalentValueBean implements Serializable {
    private String birthday;
    private int personid;//所属人才ID
    private int rank;//排序值（越大排越前）
    private int resumeAge;//年龄
    private int resumeCityId;
    private String resumeEmail;//Email
    private String resumeInfo;// "这里是自我介绍！！"，自我介绍
    private int resumeJobCategory;//1,
    private String resumeJobName;// "DJ",职位
    private String resumeJobCategoryName;
    private String resumeLabel;//"HOT",标签
    private String resumeMobile;//手机号码
    private List<ResumeMovie> resumeMovie;//视频展示
    private List<ResumeMusic> resumeMusic;//个人音乐
    private String resumeName;
    private List<ResumePicture> resumePicture;//图片展示
    private String resumeQq;//"42334578", QQ
    private int resumeSex;//0, 性别 0：为男，1：为女
    private String resumeUserbg;//详细页面里面的背景图
    private int resumeViewCount;// 浏览数
    private String resumeWorkExperience;// "这里是工作经历"，工作经历
    private String resumeWorkPlace;// "广州",城市
    private String resumeZhName;//昵称
    private int resumeid;//简历ID 通过这个简历ID调用浏览数增加的接口
    private int state;
    private String usericon;//个人图像
    private String inviteCount;//"200", 邀约数
    private String authenticity;//"80" // 真实性
    private String integrity;//80" // 诚信度
    private String transactionRecord;//合作记录


    public String getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(String inviteCount) {
        this.inviteCount = inviteCount;
    }

    public String getResumeJobCategoryName() {
        return resumeJobCategoryName;
    }

    public void setResumeJobCategoryName(String resumeJobCategoryName) {
        this.resumeJobCategoryName = resumeJobCategoryName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
        this.personid = personid;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getResumeAge() {
        return resumeAge;
    }

    public void setResumeAge(int resumeAge) {
        this.resumeAge = resumeAge;
    }

    public int getResumeCityId() {
        return resumeCityId;
    }

    public void setResumeCityId(int resumeCityId) {
        this.resumeCityId = resumeCityId;
    }

    public String getResumeEmail() {
        return resumeEmail;
    }

    public void setResumeEmail(String resumeEmail) {
        this.resumeEmail = resumeEmail;
    }

    public String getResumeInfo() {
        return resumeInfo;
    }

    public void setResumeInfo(String resumeInfo) {
        this.resumeInfo = resumeInfo;
    }

    public int getResumeJobCategory() {
        return resumeJobCategory;
    }

    public void setResumeJobCategory(int resumeJobCategory) {
        this.resumeJobCategory = resumeJobCategory;
    }

    public String getResumeJobName() {
        return resumeJobName;
    }

    public void setResumeJobName(String resumeJobName) {
        this.resumeJobName = resumeJobName;
    }

    public String getResumeLabel() {
        return resumeLabel;
    }

    public void setResumeLabel(String resumeLabel) {
        this.resumeLabel = resumeLabel;
    }

    public String getResumeMobile() {
        return resumeMobile;
    }

    public void setResumeMobile(String resumeMobile) {
        this.resumeMobile = resumeMobile;
    }

    public List<ResumeMovie> getResumeMovie() {
        return resumeMovie;
    }

    public void setResumeMovie(List<ResumeMovie> resumeMovie) {
        this.resumeMovie = resumeMovie;
    }

    public List<ResumeMusic> getResumeMusic() {
        return resumeMusic;
    }

    public void setResumeMusic(List<ResumeMusic> resumeMusic) {
        this.resumeMusic = resumeMusic;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public List<ResumePicture> getResumePicture() {
        return resumePicture;
    }

    public void setResumePicture(List<ResumePicture> resumePicture) {
        this.resumePicture = resumePicture;
    }

    public String getResumeQq() {
        return resumeQq;
    }

    public void setResumeQq(String resumeQq) {
        this.resumeQq = resumeQq;
    }

    public int getResumeSex() {
        return resumeSex;
    }

    public void setResumeSex(int resumeSex) {
        this.resumeSex = resumeSex;
    }

    public String getResumeUserbg() {
        return resumeUserbg;
    }

    public void setResumeUserbg(String resumeUserbg) {
        this.resumeUserbg = resumeUserbg;
    }

    public int getResumeViewCount() {
        return resumeViewCount;
    }

    public void setResumeViewCount(int resumeViewCount) {
        this.resumeViewCount = resumeViewCount;
    }

    public String getResumeWorkExperience() {
        return resumeWorkExperience;
    }

    public void setResumeWorkExperience(String resumeWorkExperience) {
        this.resumeWorkExperience = resumeWorkExperience;
    }

    public String getResumeWorkPlace() {
        return resumeWorkPlace;
    }

    public void setResumeWorkPlace(String resumeWorkPlace) {
        this.resumeWorkPlace = resumeWorkPlace;
    }

    public String getResumeZhName() {
        return resumeZhName;
    }

    public void setResumeZhName(String resumeZhName) {
        this.resumeZhName = resumeZhName;
    }

    public int getResumeid() {
        return resumeid;
    }

    public void setResumeid(int resumeid) {
        this.resumeid = resumeid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    @Override
    public String toString() {
        return "TalentValueBean{" +
                "birthday='" + birthday + '\'' +
                ", personid=" + personid +
                ", rank=" + rank +
                ", resumeAge=" + resumeAge +
                ", resumeCityId=" + resumeCityId +
                ", resumeEmail='" + resumeEmail + '\'' +
                ", resumeInfo='" + resumeInfo + '\'' +
                ", resumeJobCategory=" + resumeJobCategory +
                ", resumeJobName='" + resumeJobName + '\'' +
                ", resumeJobCategoryName='" + resumeJobCategoryName + '\'' +
                ", resumeLabel='" + resumeLabel + '\'' +
                ", resumeMobile='" + resumeMobile + '\'' +
                ", resumeMovie=" + resumeMovie +
                ", resumeMusic=" + resumeMusic +
                ", resumeName='" + resumeName + '\'' +
                ", resumePicture=" + resumePicture +
                ", resumeQq='" + resumeQq + '\'' +
                ", resumeSex=" + resumeSex +
                ", resumeUserbg='" + resumeUserbg + '\'' +
                ", resumeViewCount=" + resumeViewCount +
                ", resumeWorkExperience='" + resumeWorkExperience + '\'' +
                ", resumeWorkPlace='" + resumeWorkPlace + '\'' +
                ", resumeZhName='" + resumeZhName + '\'' +
                ", resumeid=" + resumeid +
                ", state=" + state +
                ", usericon='" + usericon + '\'' +
                '}';
    }
}
