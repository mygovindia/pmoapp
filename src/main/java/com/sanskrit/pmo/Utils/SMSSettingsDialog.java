package com.sanskrit.pmo.Utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;
import com.sanskrit.pmo.Models.SMSContact;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SMSSettingsDialog extends DialogFragment {
    private static final int REQUEST_CONTACT = 777;
    List<SMSContact> contacts = new ArrayList();
    TextView message;
    TextView numberContacts;


    TextView pickContacts;
    List<Contact> contactsList;

    class Token extends TypeToken<List<SMSContact>> {
        Token() {
        }
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.contacts = new ArrayList();
        View rootView = View.inflate(getContext(), R.layout.dialog_sms_settings, null);
        this.message = (TextView) rootView.findViewById(R.id.message);
        this.numberContacts = (TextView) rootView.findViewById(R.id.number_contacts);
        this.pickContacts = (TextView) rootView.findViewById(R.id.pick_contacts);
        this.message.setText(getMessage());
        this.numberContacts.setText(this.contacts.size() + " contacts selected");
        try {
            if (Reservoir.contains(Constants.CACHE_SMS_CONTACTS)) {
                try {
                    this.contacts = (List) Reservoir.get(Constants.CACHE_SMS_CONTACTS, new Token().getType());
                    if (!(this.contacts == null || this.contacts.size() == 0)) {
                        this.numberContacts.setText(this.contacts.size() + " contacts selected");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Builder dialog = new Builder(getActivity()).setTitle("SMS Configuration").setView(rootView).setPositiveButton("Save", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Utils.isMarshmallow()) {
                    SMSSettingsDialog.this.checkPermissionAndThenDismiss(SMSSettingsDialog.this.getActivity());
                } else {
                    if (contactsList != null) {
                        List<SMSContact> list = new ArrayList();
                        for (Contact contact : contactsList) {
                            SMSContact smsContact = new SMSContact();
                            smsContact.setName(contact.getFirstName() + " " + contact.getLastName());
                            smsContact.setNumber(contact.getPhone(0));
                            list.add(smsContact);
                        }
                        try {
                            Reservoir.put(Constants.CACHE_SMS_CONTACTS, list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    SMSSettingsDialog.this.dismiss();
                }
            }
        }).setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SMSSettingsDialog.this.dismiss();
            }
        });
        this.pickContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isMarshmallow()) {
                    //  SMSSettingsDialog.this.checkContactPermission(SMSSettingsDialog.this.getActivity());
                } else {
                    SMSSettingsDialog.this.launchContactPicker();
                }
            }
        });
        return dialog.create();
    }

    private String getMessage() {
        return "Carrier charges may apply when sending SMS messages. You can edit the recipient list for the SMS below";
    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    private void checkContactPermission(Activity context) {
        if (Nammu.checkPermission("android.permission.READ_CONTACTS")) {
            launchContactPicker();
        } else {
            Nammu.askForPermission(context, "android.permission.READ_CONTACTS", this.permissionContactCallback);
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionAndThenDismiss(Activity context) {
        /* if (Nammu.checkPermission("android.permission.SEND_SMS"))*/
        {

            if (contactsList != null) {
                List<SMSContact> list = new ArrayList();
                for (Contact contact : contactsList) {
                    SMSContact smsContact = new SMSContact();
                    smsContact.setName(contact.getFirstName() + " " + contact.getLastName());
                    smsContact.setNumber(contact.getPhone(0));
                    list.add(smsContact);
                }
                try {
                    Reservoir.put(Constants.CACHE_SMS_CONTACTS, list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            dismiss();


        } /*else*/
        {

            if (contactsList != null) {
                List<SMSContact> list = new ArrayList();
                for (Contact contact : contactsList) {
                    SMSContact smsContact = new SMSContact();
                    smsContact.setName(contact.getFirstName() + " " + contact.getLastName());
                    smsContact.setNumber(contact.getPhone(0));
                    list.add(smsContact);
                }
                try {
                    Reservoir.put(Constants.CACHE_SMS_CONTACTS, list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Nammu.askForPermission(context, "android.permission.SEND_SMS", this.permissionReadstorageCallback);
        }
    }

    private void launchContactPicker() {
        startActivityForResult(new Intent(getActivity(), ContactPickerActivity.class).
                        putExtra(ContactPickerActivity.EXTRA_THEME, R.style.ThemeContactPicker).
                        putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name()).
                        putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name()).
                        putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, 2).
                        putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name()),
                REQUEST_CONTACT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACT && resultCode == -1 && data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {
            contactsList = (List) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            this.numberContacts.setText(contactsList.size() + " contacts selected");

        }
    }


    final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {

            if (contactsList != null) {
                List<SMSContact> list = new ArrayList();
                for (Contact contact : contactsList) {
                    SMSContact smsContact = new SMSContact();
                    smsContact.setName(contact.getFirstName() + " " + contact.getLastName());
                    smsContact.setNumber(contact.getPhone(0));
                    list.add(smsContact);
                }
                try {
                    Reservoir.put(Constants.CACHE_SMS_CONTACTS, list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            SMSSettingsDialog.this.dismiss();
        }

        @Override
        public void permissionRefused() {
            if (SMSSettingsDialog.this.getActivity() != null) {
                Toast.makeText(SMSSettingsDialog.this.getActivity(), "Send SMS permission is required to be able to send SMS messages", Toast.LENGTH_SHORT).show();
            }
        }
    };

    final PermissionCallback permissionContactCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            SMSSettingsDialog.this.launchContactPicker();
        }

        @Override
        public void permissionRefused() {
            if (SMSSettingsDialog.this.getActivity() != null) {
                Toast.makeText(SMSSettingsDialog.this.getActivity(), "Read contacts permission is required to be able to list your contacts", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
