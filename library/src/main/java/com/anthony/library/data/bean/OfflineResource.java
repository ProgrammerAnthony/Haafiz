package com.anthony.library.data.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "tb_offline_res")
public class OfflineResource {
    @DatabaseField(generatedId = true, columnName = "res_id")
    public int res_id;

    @DatabaseField(columnName = "html_url")
    public String html_url;

    @DatabaseField(columnName = "res_url")
    public String res_url;

    @DatabaseField(columnName = "res_path")
    public String res_path;

    @DatabaseField(columnName = "res_f_name")
    public String res_f_name;
}
