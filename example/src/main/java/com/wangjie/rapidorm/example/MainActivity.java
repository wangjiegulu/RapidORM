package com.wangjie.rapidorm.example;

import com.wangjie.rapidorm.constants.RapidORMConfig;
import com.wangjie.rapidorm.example.database.DatabaseFactory;
import com.wangjie.rapidorm.example.database.dao.PersonDaoImpl;
import com.wangjie.rapidorm.example.database.model.Person;
import com.wangjie.rapidorm.util.func.RapidOrmFunc1;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView dataListTv;

    private PersonDaoImpl personDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataListTv = (TextView) findViewById(R.id.activity_main_db_data_list_tv);

        DatabaseFactory.getInstance().resetDatabase("hello_rapid_orm.db");
        personDao = new PersonDaoImpl();
        queryAll();

    }

    public void onClick(View view) {
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
            personDao.deletePerson();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void updateByBuilder() {
        try {
            personDao.updatePerson();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void queryByBuilder() {
        try {
            List<Person> personList = personDao.findPersonsByWhere();
            dataListTv.setText("query by builder" + personList.toString());
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void deleteAll() {
        try {
            personDao.deleteAll();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void insertPerformance() {
        Log.d(TAG, "insertPerformance start...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Person> persons = new ArrayList<>();
                for (int i = 0; i < 10000; i++) {
                    Person p = getPerson();
                    p.setId(1 + i);
                    p.setName("wangjie_" + i);
                    persons.add(p);
                }
                try {
                    personDao.executeInTx(new RapidOrmFunc1() {
                        @Override
                        public void call() throws Exception {
                            deleteAll();
                            RapidORMConfig.DEBUG = false;
                            long start = System.currentTimeMillis();
                            personDao.insertInTx(persons);
                            Log.i(TAG, "insert performance time: " + (System.currentTimeMillis() - start) + "ms");
                            RapidORMConfig.DEBUG = true;
                        }
                    });


                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        }).start();
    }

    private void insert() {
        try {
            personDao.insert(getPerson());
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void update() {
        Person p = new Person();
        p.setId(100);
        p.setTypeId(1);
        p.setName("wangjie_modified");
        p.setStudent(false);
        try {
            personDao.update(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        Person p = new Person();
        p.setId(100);
        p.setTypeId(1);
        try {
            personDao.delete(p);
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
            List<Person> personList = personDao.findPersons();
            dataListTv.setText("data: \n" + personList.toString());
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
