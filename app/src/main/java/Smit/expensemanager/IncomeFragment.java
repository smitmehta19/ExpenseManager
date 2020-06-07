package Smit.expensemanager;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import Smit.expensemanager.Model.Data;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {


    //Firebase database

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    //Recyclerview..

    private RecyclerView recyclerView;

    //Text view

    private TextView incomeTotalSum;

    ///Update edit text.

    private EditText edtAmmount;
    private EditText edtType;
    private EditText edtNote;

    //button for update and delete;

    private Button btnUpdate;
    private Button btnDelete;

    //Dtaa item value

    private String type;
    private String note;
    private int amount;

    private String post_key;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase=FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalSum=myview.findViewById(R.id.income_txt_result);

        recyclerView=myview.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totlatvalue = 0;

                for (DataSnapshot mysanapshot:dataSnapshot.getChildren()){

                    Data data=mysanapshot.getValue(Data.class);

                    totlatvalue+=data.getAmount();

                    String stTotalvale=String.valueOf(totlatvalue);

                    incomeTotalSum.setText(stTotalvale+".00");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return myview;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.income_recycler_data,
                        MyViewHolder.class,
                        mIncomeDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());
                viewHolder.setAmmount(model.getAmount());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key=getRef(position).getKey();

                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();

                        updateDataItem();
                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }

        private void setNote(String note){

            TextView mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);

        }

        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }

        private void setAmmount(int ammount){

            TextView mAmmount=mView.findViewById(R.id.ammount_txt_income);
            String stammount=String.valueOf(ammount);
            mAmmount.setText(stammount);

        }


    }


    private void updateDataItem(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);

        edtAmmount=myview.findViewById(R.id.ammount_edt);
        edtType=myview.findViewById(R.id.type_edt);
        edtNote=myview.findViewById(R.id.note_edt);

        //Set data to edit text..

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmmount.setText(String.valueOf(amount));
        edtAmmount.setSelection(String.valueOf(amount).length());

        btnUpdate=myview.findViewById(R.id.btn_upd_Update);
        btnDelete=myview.findViewById(R.id.btnuPD_Delete);

        final AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type=edtType.getText().toString().trim();
                note=edtNote.getText().toString().trim();

                String mdammount=String.valueOf(amount);

                mdammount=edtAmmount.getText().toString().trim();

                int myAmmount=Integer.parseInt(mdammount);

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(myAmmount,type,note,post_key,mDate);

                mIncomeDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIncomeDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();


    }


}
