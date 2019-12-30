package com.sxtx.user.model.account;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 用户实体
 * Created by Administrator on 2017/10/15.
 */
@Entity(nameInDb = "AccountModel")//在数据库中的名字，如不写则为实体中类名
public class AccountModel implements Serializable {
    private static final long serialVersionUID = -8953210336269452161L;
    @Id
    private String AccountId;
    private String ContactId;
    private String CompanyId;
    private String AbbName;//V1.0新增
    private String Name;
    private String Position;
    private String AvatarPath;
    private String Mobile;
    private String Email;
    private String Tel;
    private int ProjectAmount;
    private int EventAmount;
    private String CompanyName;
    private String DepartmentId;
    private String DepartmentName;
    private String Birthday;

    @Transient
    private boolean isFrom;//不会创建到数据库里面

    @Generated(hash = 1094969378)
    public AccountModel(String AccountId, String ContactId, String CompanyId,
            String AbbName, String Name, String Position, String AvatarPath,
            String Mobile, String Email, String Tel, int ProjectAmount,
            int EventAmount, String CompanyName, String DepartmentId,
            String DepartmentName, String Birthday) {
        this.AccountId = AccountId;
        this.ContactId = ContactId;
        this.CompanyId = CompanyId;
        this.AbbName = AbbName;
        this.Name = Name;
        this.Position = Position;
        this.AvatarPath = AvatarPath;
        this.Mobile = Mobile;
        this.Email = Email;
        this.Tel = Tel;
        this.ProjectAmount = ProjectAmount;
        this.EventAmount = EventAmount;
        this.CompanyName = CompanyName;
        this.DepartmentId = DepartmentId;
        this.DepartmentName = DepartmentName;
        this.Birthday = Birthday;
    }

    @Generated(hash = 202444181)
    public AccountModel() {
    }

    public boolean isFrom() {
        return isFrom;
    }

    public void setFrom(boolean from) {
        isFrom = from;
    }

    public String getContactId() {
        return ContactId;
    }

    public void setContactId(String contactId) {
        ContactId = contactId;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getAbbName() {
        return AbbName;
    }

    public void setAbbName(String abbName) {
        AbbName = abbName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getAvatarPath() {
        return AvatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        AvatarPath = avatarPath;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public int getProjectAmount() {
        return ProjectAmount;
    }

    public void setProjectAmount(int projectAmount) {
        ProjectAmount = projectAmount;
    }

    public int getEventAmount() {
        return EventAmount;
    }

    public void setEventAmount(int eventAmount) {
        EventAmount = eventAmount;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String departmentId) {
        DepartmentId = departmentId;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
}
