create database hotel;


-- 客房类型表
CREATE TABLE hotel_room_type
(
    type_id       bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类型ID',
    type_name     varchar(50)    NOT NULL COMMENT '类型名称',
    type_desc     varchar(200)            DEFAULT NULL COMMENT '类型描述',
    bed_count     int(4) NOT NULL DEFAULT '1' COMMENT '床位数量',
    max_occupancy int(4) NOT NULL DEFAULT '2' COMMENT '最大入住人数',
    area          decimal(10, 2)          DEFAULT NULL COMMENT '房间面积(平方米)',
    price         decimal(10, 2) NOT NULL COMMENT '房间价格(元/晚)',
    facility      varchar(500)            DEFAULT NULL COMMENT '房间设施',
    image_url     varchar(200)            DEFAULT NULL COMMENT '房间图片URL',
    image_urls    text                    DEFAULT NULL COMMENT '房间图片URL',
    status        char(1)        NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag      char(1)        NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    create_by     varchar(64)             DEFAULT '' COMMENT '创建者',
    create_time   datetime                DEFAULT NULL COMMENT '创建时间',
    update_by     varchar(64)             DEFAULT '' COMMENT '更新者',
    update_time   datetime                DEFAULT NULL COMMENT '更新时间',
    remark        varchar(500)            DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客房类型表';

-- 客房信息表
CREATE TABLE hotel_room_info
(
    room_id     bigint(20) NOT NULL AUTO_INCREMENT COMMENT '房间ID',
    room_number varchar(20) NOT NULL COMMENT '房间号',
    type_id     bigint(20) NOT NULL COMMENT '房间类型ID',
    floor       int(4) NOT NULL COMMENT '楼层',
    room_status char(1)     NOT NULL DEFAULT '0' COMMENT '房间状态（0空闲 1已预定 2已入住 3维修中）',
    del_flag    char(1)     NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)          DEFAULT '' COMMENT '创建者',
    create_time datetime             DEFAULT NULL COMMENT '创建时间',
    update_by   varchar(64)          DEFAULT '' COMMENT '更新者',
    update_time datetime             DEFAULT NULL COMMENT '更新时间',
    remark      varchar(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客房信息表';

-- 客户信息表
CREATE TABLE hotel_customer_info
(
    customer_id       bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户ID',
    real_name         varchar(30) NOT NULL COMMENT '真实姓名',
    avatar            text                 DEFAULT NULL COMMENT '头像',
    id_card           varchar(18) NOT NULL COMMENT '身份证号',
    gender            char(1)     NOT NULL DEFAULT '0' COMMENT '性别（0男 1女）',
    birthday          date                 DEFAULT NULL COMMENT '出生日期',
    address           varchar(200)         DEFAULT NULL COMMENT '地址',
    phone             varchar(11) NOT NULL COMMENT '联系电话',
    email             varchar(50)          DEFAULT NULL COMMENT '邮箱',
    emergency_contact varchar(30)          DEFAULT NULL COMMENT '紧急联系人',
    emergency_phone   varchar(11)          DEFAULT NULL COMMENT '紧急联系电话',
    create_by         varchar(64)          DEFAULT '' COMMENT '创建者',
    create_time       datetime             DEFAULT NULL COMMENT '创建时间',
    update_by         varchar(64)          DEFAULT '' COMMENT '更新者',
    update_time       datetime             DEFAULT NULL COMMENT '更新时间',
    remark            varchar(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户信息表';

-- 预定信息表
CREATE TABLE hotel_reservation_info
(
    reservation_id     bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预定ID',
    customer_id        bigint(20) NOT NULL COMMENT '客户ID',
    room_id            bigint(20) NOT NULL COMMENT '房间ID',
    reservation_code   varchar(30)    NOT NULL COMMENT '预定编号',
    check_in_date      datetime       NOT NULL COMMENT '入住日期',
    check_out_date     datetime       NOT NULL COMMENT '离店日期',
    nights             int(4) NOT NULL COMMENT '入住晚数',
    total_amount       decimal(10, 2) NOT NULL COMMENT '总金额',
    reservation_status char(1)        NOT NULL DEFAULT '0' COMMENT '预定状态（0已预定 1已确认 2已入住 3已取消 4已完成）',
    special_request    varchar(200)            DEFAULT NULL COMMENT '特殊要求',
    contact_phone      varchar(11)    NOT NULL COMMENT '联系电话',
    contact_email      varchar(50)             DEFAULT NULL COMMENT '联系邮箱',
    payment_status     char(1)        NOT NULL DEFAULT '0' COMMENT '支付状态（0未支付 1已支付 2已退款）',
    del_flag           char(1)        NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    create_by          varchar(64)             DEFAULT '' COMMENT '创建者',
    create_time        datetime                DEFAULT NULL COMMENT '创建时间',
    update_by          varchar(64)             DEFAULT '' COMMENT '更新者',
    update_time        datetime                DEFAULT NULL COMMENT '更新时间',
    remark             varchar(500)            DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (reservation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预定信息表';

-- 入住信息表
CREATE TABLE hotel_check_in_info
(
    check_in_id           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入住ID',
    reservation_id        bigint(20) NOT NULL COMMENT '预定ID',
    room_id               bigint(20) NOT NULL COMMENT '房间ID',
    customer_id           bigint(20) NOT NULL COMMENT '客户ID',
    check_in_code         varchar(30) NOT NULL COMMENT '入住编号',
    actual_check_in_date  datetime    NOT NULL COMMENT '实际入住时间',
    actual_check_out_date datetime             DEFAULT NULL COMMENT '实际离店时间',
    guest_count           int(4) NOT NULL DEFAULT '1' COMMENT '入住人数',
    id_card_front         varchar(200)         DEFAULT NULL COMMENT '身份证正面照片URL',
    id_card_back          varchar(200)         DEFAULT NULL COMMENT '身份证反面照片URL',
    check_in_status       char(1)     NOT NULL DEFAULT '0' COMMENT '入住状态（0入住中 1已离店）',
    del_flag              char(1)     NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    create_by             varchar(64)          DEFAULT '' COMMENT '创建者',
    create_time           datetime             DEFAULT NULL COMMENT '创建时间',
    update_by             varchar(64)          DEFAULT '' COMMENT '更新者',
    update_time           datetime             DEFAULT NULL COMMENT '更新时间',
    remark                varchar(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (check_in_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入住信息表';

-- 支付信息表
CREATE TABLE hotel_payment_info
(
    payment_id     bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付ID',
    reservation_id bigint(20) NOT NULL COMMENT '预定ID',
    payment_amount decimal(10, 2) NOT NULL COMMENT '支付金额',
    payment_method char(1)        NOT NULL DEFAULT '0' COMMENT '支付方式（0支付宝 1微信 2银行卡 3现金）',
    payment_status char(1)        NOT NULL DEFAULT '0' COMMENT '支付状态（0待支付 1已支付 2已退款）',
    transaction_id varchar(100)            DEFAULT NULL COMMENT '交易流水号',
    payment_time   datetime                DEFAULT NULL COMMENT '支付时间',
    refund_time    datetime                DEFAULT NULL COMMENT '退款时间',
    del_flag       char(1)        NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    create_by      varchar(64)             DEFAULT '' COMMENT '创建者',
    create_time    datetime                DEFAULT NULL COMMENT '创建时间',
    update_by      varchar(64)             DEFAULT '' COMMENT '更新者',
    update_time    datetime                DEFAULT NULL COMMENT '更新时间',
    remark         varchar(500)            DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (payment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付信息表';


-- 插入客房类型数据
INSERT INTO hotel_room_type (type_name, type_desc, bed_count, max_occupancy, area, price, facility, status, create_time)
VALUES ('标准间', '配备一张大床或两张单人床的标准客房', 2, 2, 25.00, 388.00, '免费WiFi,空调,电视,独立卫浴', '0', NOW()),
       ('豪华间', '空间宽敞，装修豪华的客房', 1, 2, 35.00, 688.00, '免费WiFi,空调,电视,独立卫浴,浴缸,迷你吧', '0',
        NOW()),
       ('商务套房', '适合商务人士的套房', 1, 3, 50.00, 1288.00, '免费WiFi,空调,电视,独立卫浴,浴缸,办公区,客厅', '0',
        NOW()),
       ('总统套房', '酒店最高级别的套房', 2, 4, 100.00, 2888.00,
        '免费WiFi,空调,电视,独立卫浴,按摩浴缸,桑拿,厨房,客厅,餐厅', '0', NOW());

-- 插入客房信息数据
INSERT INTO hotel_room_info (room_number, type_id, floor, room_status, create_time)
VALUES ('101', 1, 1, '0', NOW()),
       ('102', 1, 1, '0', NOW()),
       ('103', 1, 1, '0', NOW()),
       ('201', 2, 2, '0', NOW()),
       ('202', 2, 2, '0', NOW()),
       ('301', 3, 3, '0', NOW()),
       ('401', 4, 4, '0', NOW());

-- 插入客户信息数据
INSERT INTO hotel_customer_info (real_name, id_card, gender, birthday, address, phone, email, emergency_contact,
                                 emergency_phone, create_time)
VALUES ('张三', '110101199001011234', '0', '1990-01-01', '北京市朝阳区xxx街道', '13800138001', 'zhangsan@example.com',
        '张母', '13800138003', NOW()),
       ('李四', '110101199202021235', '1', '1992-02-02', '上海市浦东新区xxx街道', '13800138002', 'lisi@example.com',
        '李父', '13800138004', NOW());

-- 插入预定信息数据
INSERT INTO hotel_reservation_info (customer_id, room_id, reservation_code, check_in_date, check_out_date, nights,
                                    total_amount, reservation_status, contact_phone, payment_status, create_time)
VALUES (1, 1, 'RES20230801001', '2023-08-10 14:00:00', '2023-08-12 12:00:00', 2, 776.00, '1', '13800138001', '1',
        NOW()),
       (2, 4, 'RES20230801002', '2023-08-15 14:00:00', '2023-08-17 12:00:00', 2, 1376.00, '0', '13800138002', '0',
        NOW());

-- 插入支付信息数据
INSERT INTO hotel_payment_info (reservation_id, payment_amount, payment_method, payment_status, transaction_id,
                                payment_time, create_time)
VALUES (1, 776.00, '1', '1', 'TX202308010001', '2023-08-01 10:30:00', NOW());