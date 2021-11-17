/*
Programmer: Jackson Landry 100302201
Date: 17-11-2021
Purpose: This is the filter, which is used when the user uses the search bar to look for a specific entry in the database.
 */

package com.example.assignmenttwo;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    ArrayList<Model> filterList;
    Adapter adapter;

    //constructor for custom filter
    public CustomFilter(ArrayList<Model> filterList, Adapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    //method to perform the filtering of the database with constraint
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        //constraint not null and has length
        if(constraint != null && constraint.length() > 0){

            //sets constraint to upper case for comparison so that the user can enter the search in any case
            constraint = constraint.toString().toUpperCase();

            ArrayList<Model> filterModels = new ArrayList<>();

            //filters the list. if the database contains part of the address being entered by the user, it is added to the filter
            for(int i = 0; i < filterList.size(); i++){
                if(filterList.get(i).getAddress().toUpperCase().contains(constraint)){
                    filterModels.add(filterList.get(i));
                }
            }

            results.count = filterModels.size();
            results.values = filterModels;
        }
        else{
            results.count = filterList.size();
            results.values = filterList;
        }
        //returns filtered results
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.arrayList = (ArrayList<Model>) results.values;
        adapter.notifyDataSetChanged();
    }
}
