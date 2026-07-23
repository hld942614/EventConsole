package com.project.uhdbackend.enums;

/** 對應 CMDB_ASSET_NETWORK.IP_TYPE / IPAM_IP_ADDRESS.IP_TYPE 的 CHECK 約束 */
public enum IpType {
    USER_LAN,
    BACKUP,
    IDRAC,
    MANAGEMENT,
    SERVICE,
    VIP,
    NAT,
    OTHER
}
