package com.example.numad25sp_linyanfu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements ContactAdapter.ContactClickListener {
    private static final int CALL_PERMISSION_REQUEST_CODE = 123;
    private List<Contact> contacts;
    private ContactAdapter adapter;
    private Contact pendingCallContact;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Create a temporary ArrayList to hold contact data
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> phones = new ArrayList<>();
        
        for (Contact contact : contacts) {
            names.add(contact.getName());
            phones.add(contact.getPhoneNumber());
        }
        
        outState.putStringArrayList("contact_names", names);
        outState.putStringArrayList("contact_phones", phones);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contacts = new ArrayList<>();
        setupRecyclerView();
        setupFAB();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(contacts, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fabAddContact);
        fab.setOnClickListener(v -> showAddContactDialog(null, -1));
    }

    private void showAddContactDialog(Contact contactToEdit, int position) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        EditText nameInput = dialogView.findViewById(R.id.nameInput);
        EditText phoneInput = dialogView.findViewById(R.id.phoneInput);

        if (contactToEdit != null) {
            nameInput.setText(contactToEdit.getName());
            phoneInput.setText(contactToEdit.getPhoneNumber());
        }

        new AlertDialog.Builder(this)
                .setTitle(contactToEdit == null ? "Add New Contact" : "Edit Contact")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameInput.getText().toString();
                    String phone = phoneInput.getText().toString();

                    if (name.isEmpty() || phone.isEmpty()) {
                        showSnackbar("Please fill in all fields", null, null);
                        return;
                    }

                    if (contactToEdit == null) {
                        Contact newContact = new Contact(name, phone);
                        contacts.add(newContact);
                        adapter.notifyItemInserted(contacts.size() - 1);
                        showSnackbar("Contact added successfully", "Call Now", 
                            v -> initiateCall(newContact));
                    } else {
                        contactToEdit.setName(name);
                        contactToEdit.setPhoneNumber(phone);
                        adapter.notifyItemChanged(position);
                        showSnackbar("Contact updated successfully", "Call Now", 
                            v -> initiateCall(contactToEdit));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onContactClick(Contact contact) {
        pendingCallContact = contact;
        if (checkCallPermission()) {
            initiateCall(contact);
        }
    }

    @Override
    public void onEditClick(Contact contact, int position) {
        showAddContactDialog(contact, position);
    }

    @Override
    public void onDeleteClick(Contact contact, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    contacts.remove(position);
                    adapter.notifyItemRemoved(position);
                    showSnackbar("Contact deleted", "Undo", v -> {
                        contacts.add(position, contact);
                        adapter.notifyItemInserted(position);
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private boolean checkCallPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingCallContact != null) {
                    initiateCall(pendingCallContact);
                    pendingCallContact = null;
                }
            } else {
                showSnackbar("Call permission denied", null, null);
            }
        }
    }

    private void initiateCall(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
        startActivity(intent);
    }

    private void showSnackbar(String message, String actionLabel, View.OnClickListener action) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), 
            message, Snackbar.LENGTH_LONG);
        if (actionLabel != null && action != null) {
            snackbar.setAction(actionLabel, action);
        }
        snackbar.show();
    }
} 