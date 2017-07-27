create table emb_wx_user_register
(
   id                   bigint(20) not null auto_increment comment '主键id',
   wxopenid             varchar(200) comment '微信用户openid',
   create_time          datetime comment '创建时间',
   sex                  tinyint(4) comment '性别',
   language             varchar(200) comment '用户的语言，简体中文为zh_CN',
   country              varchar(255) comment '国家',
   province             varchar(255) comment '省',
   city                 varchar(255) comment '市',
   head_image_url       varchar(500) comment '微信头像',
   subscribe_time       bigint(20) comment '关注时间',
   subcribe             tinyint(4) comment '取消关注与否 0-取消关注',
   nick_name            varchar(500) comment '微信昵称',
   param                varchar(255) comment '参数',
   unionid              varchar(255) comment '微信内置UnionID机制',
   tickets              varchar(255) comment '微信二维码tickets',
   remark               varchar(255) comment '公众号运营者对粉丝的备注',
   group_id             bigint(20) comment '用户所在的分组ID',
   primary key (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8 comment '微信用户注册表';