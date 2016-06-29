package com.wangjie.rapidorm.example;

import com.wangjie.androidinject.annotation.annotations.base.AIClick;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.rapidorm.constants.RapidORMConfig;
import com.wangjie.rapidorm.example.database.DatabaseFactory;
import com.wangjie.rapidorm.example.database.dao.PersonDaoImpl;
import com.wangjie.rapidorm.example.database.model.Person;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@AILayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @AIView(R.id.activity_main_db_data_list_tv)
    private TextView dataListTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseFactory.getInstance().resetDatabase("hello_rapid_orm.db");
        queryAll();

    }


    @Override
    @AIClick({R.id.activity_main_insert_btn, R.id.activity_main_insert_performance_btn,
            R.id.activity_main_update_btn, R.id.activity_main_delete_btn,
            R.id.activity_main_delete_all_btn, R.id.activity_main_query_by_builder_btn,
            R.id.activity_main_update_by_builder_btn, R.id.activity_main_delete_by_builder_btn
    })
    public void onClickCallbackSample(View view) {
        switch (view.getId()) {
            case R.id.activity_main_insert_btn:
                insert();
                queryAll();
                break;
            case R.id.activity_main_insert_performance_btn:
                insertPerformance();
                break;
            case R.id.activity_main_update_btn:
                update();
                queryAll();
                break;
            case R.id.activity_main_delete_btn:
                delete();
                queryAll();
                break;
            case R.id.activity_main_delete_all_btn:
                deleteAll();
                queryAll();
                break;

            case R.id.activity_main_query_by_builder_btn:
                queryByBuilder();
                break;
            case R.id.activity_main_update_by_builder_btn:
                updateByBuilder();
                queryAll();
                break;
            case R.id.activity_main_delete_by_builder_btn:
                deleteByBuilder();
                queryAll();
                break;

            default:
                break;
        }
    }

    private void deleteByBuilder() {
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).deletePerson();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void updateByBuilder() {
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).updatePerson();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void queryByBuilder() {
        try {
            List<Person> personList = DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).findPersonsByWhere();
            dataListTv.setText("query by builder" + personList.toString());
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void deleteAll() {
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).deleteAll();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void insertPerformance() {
        Log.d(TAG, "insertPerformance start...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteAll();
                RapidORMConfig.DEBUG = false;
                List<Person> persons = new ArrayList<>();
                for (int i = 0; i < 5000; i++) {
                    Person p = getPerson();
                    p.setId(1 + i);
                    p.setName("wangjie_" + i);
                    persons.add(p);
                }
                long start = System.currentTimeMillis();
                try {
                    DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).insertInTx(persons);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                Log.i(TAG, "insert performance time: " + (System.currentTimeMillis() - start) + "ms");
                RapidORMConfig.DEBUG = true;
//                deleteAll();
            }
        }).start();
    }

//    private void insertPerformance() {
//        Log.d(TAG, "insertPerformance start...");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                deleteAll();
//                List<Person> persons = new ArrayList<>();
//                for (int i = 0; i < 5000; i++) {
//                    Person p = getPerson();
//                    p.setId(1 + i);
//                    p.setName("wangjie_" + i);
//                    persons.add(p);
//                }
//
//                long start = System.currentTimeMillis();
//                for(Person person : persons){
//                    try {
//                        DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).executeInsert(person, DatabaseProcessor.getInstance().getDb(), SqlUtil.getInsertColumnConfigs(DatabaseProcessor.getInstance().getTableConfig(Person.class)));
//                    } catch (Exception e) {
//                        Log.e(TAG, "", e);
//                    }
//                }
//                Log.i(TAG, "insert performance time: " + (System.currentTimeMillis() - start));
////                deleteAll();
//            }
//        }).start();
//    }


    private void insert() {
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).insert(getPerson());
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void update() {
        showToastMessage("update");
        Person p = new Person();
        p.setId(100);
        p.setTypeId(1);
        p.setName("wangjie_modified");
        p.setStudent(false);
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).update(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        showToastMessage("delete");
        Person p = new Person();
        p.setId(100);
        p.setTypeId(1);
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).delete(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Person getPerson() {
        Person person = new Person();
        person.setId(100);
        person.setTypeId(1);
        person.setName("wangjie");
        person.setAge(29);
        person.setBirth(System.currentTimeMillis());
        person.setAddress("address");
        person.setStudent(true);
        return person;
    }

    private void queryAll() {
        try {
            List<Person> personList = DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).findPersons();
            dataListTv.setText("last 10 datas: " + personList.toString());
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
