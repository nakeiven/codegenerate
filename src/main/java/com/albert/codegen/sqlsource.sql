create table `iqc_sn_collection_log` (
	`id` bigint unsigned  not null auto_increment comment '主键',
	`gmt_create` datetime  not null comment '创建时间',
	`gmt_modified` datetime  not null comment '修改时间',
	`sn_collection_id` bigint unsigned  not null comment 'sn采集单id',
	`warehouse_id` bigint unsigned  not null comment '仓库id',
	`owner_id` bigint unsigned  not null comment '货主id',
	`item_id` bigint unsigned  not null comment '商品id',
	`serial_num` varchar(128)  not null comment 'sn序列号',
	`operate_type` int unsigned  not null comment '操作类型',
	`operator_id` bigint unsigned  not null comment '操作人id',
	primary key (id)
) comment='sn序列号记录表';