package edu.com.app.data.db;

import android.database.sqlite.SQLiteDatabase;

import edu.com.app.data.bean.Channel;
import edu.com.app.data.bean.Menu;


/**
 * Created by Anthony on 2016/7/25.
 * Class Note:
 * data just for testing
 */
public class TestData {
    SQLiteDatabase mDb;

    public TestData(SQLiteDatabase db) {
        this.mDb = db;
    }

    public void initTestList() {
        //list 1
        long firstListId = mDb.insert(TestList.TABLE, null, new TestList.Builder()
                .name("Friend Group 1 ")
                .build());
        mDb.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(firstListId)
                .description("1A")
                .build());
        mDb.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(firstListId)
                .description("1B")
                .build());
        mDb.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(firstListId)
                .description("1C")
                .build());

//list 2
        long secondListId = mDb.insert(TestList.TABLE, null, new TestList.Builder()
                .name("Friend Group 2")
                .build());
        mDb.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(secondListId)
                .description("2A")
                .build());
        mDb.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(secondListId)
                .description("2B")
                .build());
        mDb.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(secondListId)
                .description("2C")
                .build());
        mDb.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(secondListId)
                .description("2D")
                .build());
//       mDb.insert(TestItem.TABLE, null, new TestItem.Builder()
//               .listId(secondListId)
//               .description("2D")
//               .build());
    }


    public void initTestMenu() {
        long fistMenuId = mDb.insert(Menu.TABLE, null,
                new Menu.Builder().type("FirstMenu").build());

        mDb.insert(Channel.TABLE, null, new Channel.Builder().title("聊天")
                .isFix(0)
                .isSubscribe(0)
                .lrt(0)
                .sort(0)
                .img("test test")
                .menuId(fistMenuId)
                .type("ChattingListFragment")
                .url("raw://news_menu").build());
        mDb.insert(Channel.TABLE, null, new Channel.Builder().title("好友")
                .isFix(0)
                .isSubscribe(0)
                .lrt(0)
                .sort(0)
                .img("test test")
                .menuId(fistMenuId)
                .type("FriendsListFragment")
                .url("raw://news_menu").build());
        mDb.insert(Channel.TABLE, null, new Channel.Builder().title("发现")
                .isFix(0)
                .isSubscribe(0)
                .lrt(0)
                .sort(0)
                .img("test test")
                .menuId(fistMenuId)
                .type("FindFragment")
                .url("raw://news_menu").build());
        mDb.insert(Channel.TABLE, null, new Channel.Builder().title("资讯")
                .isFix(0)
                .isSubscribe(0)
                .lrt(0)
                .sort(0)
                .img("test test")
                .menuId(fistMenuId)
                .type("NewsFragment")
                .url("raw://news_menu").build());
    }

}
