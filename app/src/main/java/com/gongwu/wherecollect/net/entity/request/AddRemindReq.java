package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class AddRemindReq extends RequestBase {
    public String uid;
    public String title;
    public String description;
    public String tips_time;
    public String first;
    public String repeat;
    public String associated_object_id;
    public String associated_object_url;
    public String device_token;
    public String remind_id;
}
