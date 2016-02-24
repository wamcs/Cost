package com.example.cost.Utils;

import com.example.cost.datebase.BillDataHelper;

/**
 * author:wamcs
 * date:2016/2/23
 * email:kaili@hustunique.com
 */
public class DataBaseHelper {

    private static final String DATABASE_NAME="bills.db";

    private static class DateBaseHolder{
        private static BillDataHelper billDataHelper =new BillDataHelper(AppData.getContext(),DATABASE_NAME,1);
    }

    public static BillDataHelper getInstance(){
        return DateBaseHolder.billDataHelper;
    }
}
