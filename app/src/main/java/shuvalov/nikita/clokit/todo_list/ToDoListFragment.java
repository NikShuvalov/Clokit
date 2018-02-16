package shuvalov.nikita.clokit.todo_list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;


public class ToDoListFragment extends Fragment implements FloatingActionButton.OnClickListener{
    private ToDoRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private OnFragmentInteractionListener mListener;

    public ToDoListFragment() {
        // Required empty public constructor
    }

    public static ToDoListFragment newInstance() {
        return new ToDoListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.todo_recycler);
        view.findViewById(R.id.fab).setOnClickListener(this);
        initRecycler();
        return view;
    }

    private void initRecycler(){
        mAdapter = new ToDoRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder toDoAlertBuilder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_to_do,null);
        toDoAlertBuilder.setView(dialogView);
        final EditText titleEditText = (EditText)dialogView.findViewById(R.id.title_edit_text);
        final EditText descriptionEditText  =(EditText)dialogView.findViewById(R.id.description_edit_text);

        toDoAlertBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String description = descriptionEditText.getText().toString();
                String title = titleEditText.getText().toString();
                ToDoItem toDoItem = new ToDoItem(title, description, Calendar.getInstance().getTimeInMillis(), 0);
                ToDoListManager.getInstance().addToDoItem(toDoItem);
                GoalSQLHelper.getInstance(getContext()).addToDoItem(toDoItem);
                mAdapter.refreshItems();
            }
        }).create().show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
