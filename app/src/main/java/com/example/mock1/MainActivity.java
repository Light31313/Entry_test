package com.example.mock1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mock1.adapter.PhuongTienAdapter;
import com.example.mock1.comparator.SortByPrice;
import com.example.mock1.dao.PhuongTienDao;
import com.example.mock1.databinding.ActivityMainBinding;
import com.example.mock1.databinding.DialogDeleteBinding;
import com.example.mock1.databinding.DialogFindGreaterPriceBinding;
import com.example.mock1.databinding.DialogInsertPhuongTienBinding;
import com.example.mock1.databinding.DialogUpdateBinding;
import com.example.mock1.entity.Category;
import com.example.mock1.entity.PhuongTien;
import com.example.mock1.utils.AppDatabase;
import com.example.mock1.utils.VNCharacterUtils;
import com.example.mock1.viewModel.MainViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PhuongTienDao phuongTienDao;
    private List<String> categories;
    private MainViewModel viewModel;
    private List<PhuongTien> phuongTiens;
    private PhuongTienAdapter adapter;
    private ArrayAdapter<String> ad;
    private ArrayAdapter<String> namesAdapter;
    private SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.tbMenu);
        registerForContextMenu(binding.rvPhuongTienList);

        initComponent();
        initEvent();
    }

    private void initComponent() {

        phuongTienDao = AppDatabase.getInstance(this).phuongTienDao();
        phuongTiens = phuongTienDao.getAll();
        initPhuongTienList();
        addCategory();
        ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void addCategory() {
        categories = new ArrayList<>();
        categories.add(Category.CATEGORY1);
        categories.add(Category.CATEGORY2);
        categories.add(Category.CATEGORY3);
        categories.add(Category.CATEGORY4);
        categories.add(Category.CATEGORY5);
    }

    private void initPhuongTienList() {
        adapter = new PhuongTienAdapter(this, phuongTiens);
        binding.rvPhuongTienList.setAdapter(adapter);
        binding.rvPhuongTienList.setLayoutManager(new LinearLayoutManager(this));
        updateAutoCompleteSearch();
    }

    private void updateAutoCompleteSearch(){
        String[] names = phuongTienDao.getAllNames();
        namesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names);
        if (searchAutoComplete != null) {
            searchAutoComplete.setAdapter(namesAdapter);
        }
    }

    private void initEvent() {
        viewModel.getUpdate().observe(this, isUpdated -> initPhuongTienList());
    }

    private void insertDialog() {
        Dialog dialog = new Dialog(this);
        DialogInsertPhuongTienBinding dialogBinding = DialogInsertPhuongTienBinding.inflate(dialog.getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        TextInputEditText edtId = dialogBinding.edtId;
        TextInputEditText edtPrice = dialogBinding.edtPrice;
        TextInputEditText edtName = dialogBinding.edtName;
        Spinner spCategory = dialogBinding.spCategory;


        spCategory.setAdapter(ad);
        edtId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(dialogBinding.edtId.getText()).toString().length() <= 0) {
                    dialogBinding.edtId.setError("Không được để trống");

                }
            }
        });
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(dialogBinding.edtName.getText()).toString().length() <= 0) {
                    dialogBinding.edtName.setError("Không được để trống");
                }
            }
        });
        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(dialogBinding.edtPrice.getText()).toString().length() <= 0) {
                    dialogBinding.edtPrice.setError("Không được để trống");
                }
            }
        });

        dialogBinding.btnConfirm.setOnClickListener(view -> {
            if (Objects.requireNonNull(edtId.getText()).toString().equals(""))
                edtId.setError("Không được để trống");
            else if (Objects.requireNonNull(edtName.getText()).toString().trim().equals(""))
                edtName.setError("Không được để trống");
            else if (Objects.requireNonNull(edtPrice.getText()).toString().equals(""))
                edtPrice.setError("Không được để trống");
            else {
                int id = Integer.parseInt(edtId.getText().toString());
                String name = edtName.getText().toString().trim();
                String category = spCategory.getSelectedItem().toString().trim();
                long price = Integer.parseInt(edtPrice.getText().toString());

                PhuongTien phuongTien = new PhuongTien(id, name, category, price);
                try {
                    phuongTienDao.create(phuongTien);
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(this, "Thêm thất bại vì ID bị trùng", Toast.LENGTH_LONG).show();
                }
                phuongTiens = phuongTienDao.getAll();
                viewModel.setUpdate(true);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void deleteDialog() {
        Dialog dialog = new Dialog(this);
        DialogDeleteBinding dialogBinding = DialogDeleteBinding.inflate(dialog.getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        TextInputEditText edtEnterId = dialogBinding.edtEnterId;
        dialogBinding.btnConfirm.setOnClickListener(view -> {
            if (Objects.requireNonNull(edtEnterId.getText()).toString().equals(""))
                edtEnterId.setError("Không được để trống");
            else {
                int pTId = Integer.parseInt(edtEnterId.getText().toString());

                PhuongTien phuongTien = phuongTienDao.getById(pTId);
                if (phuongTien == null)
                    Toast.makeText(this, "Không tồn tại phương tiện có id = "+pTId, Toast.LENGTH_SHORT).show();
                else {
                    phuongTienDao.deleteById(pTId);
                    phuongTiens = phuongTienDao.getAll();
                    viewModel.setUpdate(true);
                    dialog.dismiss();
                }
            }

        });
        dialog.show();
    }

    private void updateDialog() {
        Dialog dialog = new Dialog(this);
        DialogUpdateBinding dialogBinding = DialogUpdateBinding.inflate(dialog.getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.spCategory.setAdapter(ad);
        TextInputEditText edtId = dialogBinding.edtId;
        TextInputEditText edtPrice = dialogBinding.edtPrice;
        TextInputEditText edtName = dialogBinding.edtName;
        Spinner spCategory = dialogBinding.spCategory;


        spCategory.setAdapter(ad);
        edtId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(dialogBinding.edtId.getText()).toString().length() <= 0) {
                    dialogBinding.edtId.setError("Không được để trống");
                }
            }
        });
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(dialogBinding.edtName.getText()).toString().length() <= 0) {
                    dialogBinding.edtName.setError("Không được để trống");
                }
            }
        });
        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(dialogBinding.edtPrice.getText()).toString().length() <= 0) {
                    dialogBinding.edtPrice.setError("Không được để trống");
                }
            }
        });
        dialogBinding.btnConfirm.setOnClickListener(view -> {
            if (Objects.requireNonNull(edtId.getText()).toString().equals(""))
                edtId.setError("Không được để trống");
            else if (Objects.requireNonNull(edtName.getText()).toString().trim().equals(""))
                edtName.setError("Không được để trống");
            else if (Objects.requireNonNull(edtPrice.getText()).toString().equals(""))
                edtPrice.setError("Không được để trống");
            else {
                int id = Integer.parseInt(dialogBinding.edtId.getText().toString());
                String name = dialogBinding.edtName.getText().toString().trim();
                String category = dialogBinding.spCategory.getSelectedItem().toString();
                long price = Integer.parseInt(dialogBinding.edtPrice.getText().toString());
                PhuongTien phuongTien = phuongTienDao.getById(id);
                if (phuongTien == null)
                    Toast.makeText(this, "Không tồn tại phương tiện có id = " + id, Toast.LENGTH_SHORT).show();
                else {
                    phuongTienDao.updateByID(id, name, category, price);
                    phuongTiens = phuongTienDao.getAll();
                    viewModel.setUpdate(true);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void findGreaterPriceDialog() {
        Dialog dialog = new Dialog(this);
        DialogFindGreaterPriceBinding dialogBinding = DialogFindGreaterPriceBinding.inflate(dialog.getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        TextInputEditText edtEnterPrice = dialogBinding.edtEnterPrice;
        dialogBinding.btnConfirm.setOnClickListener(view -> {
            if (Objects.requireNonNull(edtEnterPrice.getText()).toString().equals(""))
                edtEnterPrice.setError("Không được để trống");
            else {
                long price = Long.parseLong(edtEnterPrice.getText().toString());
                phuongTiens = phuongTienDao.getGreaterThanPrice(price);
                viewModel.setUpdate(true);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_find_by_name);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //Get searchView autoComplete object
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        // Create a new ArrayAdapter and add data to search auto complete object.
        searchAutoComplete.setAdapter(namesAdapter);
        searchAutoComplete.setThreshold(1);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            String queryString = (String) adapterView.getItemAtPosition(itemIndex);
            searchAutoComplete.setText(queryString);
            phuongTiens = phuongTienDao.getByName(queryString);
            viewModel.setUpdate(true);
        });


        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_insert)
            insertDialog();
        else if (id == R.id.action_delete_by_id)
            deleteDialog();
        else if (id == R.id.action_update_by_id)
            updateDialog();
        else if (id == R.id.action_sort_by_name) {
            VNCharacterUtils vnCharacterUtils = new VNCharacterUtils();
            Collections.sort(phuongTiens, (o1, o2) -> vnCharacterUtils.generator(o1.getName()).compareTo(vnCharacterUtils.generator(o2.getName())));
            adapter.notifyDataSetChanged();
        } else if (id == R.id.action_sort_by_price) {
            Collections.sort(phuongTiens, new SortByPrice());
            adapter.notifyDataSetChanged();
        } else if (id == R.id.action_find_greater_price)
            findGreaterPriceDialog();
        else if (id == R.id.action_show_all) {
            phuongTiens = phuongTienDao.getAll();
            viewModel.setUpdate(true);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position;
        try {
            position = adapter.getPosition();
        } catch (Exception e) {
            Log.d("AAA", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }

        int id = item.getItemId();
        if (id == R.id.action_delete) {
            phuongTienDao.deleteById(phuongTiens.get(position).getId());
            phuongTiens.remove(phuongTiens.get(position));
            adapter.notifyItemRemoved(position);
            updateAutoCompleteSearch();
        } else if (id == R.id.action_update) {
            int itemID = phuongTiens.get(position).getId();
            updateItemDialog(itemID, position);
        }
        return super.onContextItemSelected(item);
    }

    private void updateItemDialog(int id, int position) {
        Dialog dialog = new Dialog(this);
        DialogUpdateBinding dialogBinding = DialogUpdateBinding.inflate(dialog.getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.spCategory.setAdapter(ad);
        TextInputEditText edtId = dialogBinding.edtId;
        TextInputEditText edtPrice = dialogBinding.edtPrice;
        TextInputEditText edtName = dialogBinding.edtName;
        Spinner spCategory = dialogBinding.spCategory;
        String textId = "" + id;

        edtId.setText(textId);
        edtId.setEnabled(false);

        spCategory.setAdapter(ad);

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(dialogBinding.edtName.getText()).toString().length() <= 0) {
                    dialogBinding.edtName.setError("Không được để trống");
                }
            }
        });
        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(dialogBinding.edtPrice.getText()).toString().length() <= 0) {
                    dialogBinding.edtPrice.setError("Không được để trống");
                }
            }
        });
        dialogBinding.btnConfirm.setOnClickListener(view -> {
            if (Objects.requireNonNull(edtName.getText()).toString().trim().equals(""))
                edtName.setError("Không được để trống");
            else if (Objects.requireNonNull(edtPrice.getText()).toString().equals(""))
                edtPrice.setError("Không được để trống");
            else {
                String name = dialogBinding.edtName.getText().toString().trim();
                String category = dialogBinding.spCategory.getSelectedItem().toString();
                long price = Integer.parseInt(dialogBinding.edtPrice.getText().toString());

                phuongTienDao.updateByID(id, name, category, price);
                PhuongTien phuongTien = phuongTienDao.getById(id);
                phuongTiens.remove(position);
                phuongTiens.add(position, phuongTien);
                adapter.notifyItemChanged(position);
                updateAutoCompleteSearch();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}