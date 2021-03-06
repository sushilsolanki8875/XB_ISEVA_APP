package com.iseva.app.source.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Realm_Seat_Details;
import com.iseva.app.source.Realm_objets.Realm_Selected_Seats;
import com.iseva.app.source.travel.Activity_Select_Seats;
import com.iseva.app.source.travel.Constants.SEAT_DETAILS;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Deck2 extends Fragment_Parent_Deck {


    int Total_screen_width;
    double Seat_height_percentage = 100;
    double Seat_width_percentage = 11;

    double fix_left_per = 5.50;
    double fix_right_per = 5.50;

    int fix_margin_left = 0;
    int fix_margin_right =0;
    public int max_col = 0;
    int Seat_height;
    int Seat_width;
    int all_col;
    int margin;
    int extra_height = 5;
    double margin_top_seat_per = 4.50;
    int margin_top_seat = 0;
    int fixtopmargin = 30;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__deck2, container, false);
        My_realm = Realm.getDefaultInstance();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

//        colm_numbers = new ArrayList<Integer>();
        RealmResults<Realm_Seat_Details> all_seats = My_realm.where(Realm_Seat_Details.class).equalTo("Deck",2).notEqualTo("SeatNo",".DOOR").findAll();



//        for(int l=0;l<all_seats.size();l++)
//        {
//
//            if(!colm_numbers.contains(all_seats.get(l).getCol()))
//                {
//                    colm_numbers.add(all_seats.get(l).getCol());
//                }
//            }
//
//        Collections.sort(colm_numbers);

//        colm_numbers.add((colm_numbers.size()/2),99);

//        max_col = colm_numbers.size();
        Log.e("vikas all seat=",""+all_seats.size());
        Log.e("vikas max col=",""+max_col);


        layout = (FrameLayout)view.findViewById(R.id.layout_deck2);

        Total_screen_width = displaymetrics.widthPixels;

        fix_margin_left = (int)(Total_screen_width * fix_left_per)/100;
        fix_margin_right = (int)(Total_screen_width * fix_right_per)/100;

        margin_top_seat = (int)(Total_screen_width * margin_top_seat_per)/100;

        Seat_width = (int)(Total_screen_width * Seat_width_percentage)/100;
        Seat_height = (int)(Seat_width * Seat_height_percentage)/100;
        margin = (int)(Total_screen_width-((Seat_width *max_col)+(fix_margin_left+fix_margin_right)))/(max_col-1);




        for(int i=0;i < all_seats.size(); i++)
        {

                    int row = all_seats.get(i).getRow();
                    int col = all_seats.get(i).getCol();

                    final int height = all_seats.get(i).getHeight();
                    final int width = all_seats.get(i).getWidth();

                    if(height == 2 && width == 1)
                    {
                        int realseatheight = (Seat_height*height*92)/100;
                        params = new FrameLayout.LayoutParams(Seat_width,realseatheight);
                        params.topMargin = ((margin_top_seat+Seat_height+extra_height)*(row))+fixtopmargin;
                    }
                    else if(height == 1 && width == 2)
                    {
                        params = new FrameLayout.LayoutParams(Seat_width*width,Seat_height*height);
                        params.topMargin = ((margin_top_seat+Seat_height+extra_height)*(row))+fixtopmargin;
                    }
                    else
                    {
                        params = new FrameLayout.LayoutParams(Seat_width,(Seat_height*height)+extra_height);
                        params.topMargin = ((margin_top_seat+Seat_height+extra_height)*(row))+fixtopmargin;
                    }

                    params.leftMargin = ((Seat_width+margin)*(col)+fix_margin_left);


                    Seat_view = new TextView(getActivity());
                    Seat_view.setTextSize(10);
                    Seat_view.setLayoutParams(params);
                    Seat_view.setGravity(Gravity.CENTER);
                    Seat_view.setText(all_seats.get(i).getSeatNo());
                    Seat_view.setTag(R.string.SeatNo,all_seats.get(i).getSeatNo());
                    Seat_view.setTag(R.string.IsSelected,"false");
                    Seat_view.setTag(R.string.Deck,all_seats.get(i).getDeck());
                    Seat_view.setTag(R.string.Gender,all_seats.get(i).getGender());
                    Seat_view.setTag(R.string.Fare,all_seats.get(i).getFare());
                    Seat_view.setTag(R.string.Offer_Fare,all_seats.get(i).getFare_after_offer());
                    Seat_view.setTag(R.string.IsSleeper,all_seats.get(i).getIsSleeper());
                    Seat_view.setTag(R.string.Seat_Type,all_seats.get(i).getSeatType());


                    if(all_seats.get(i).getIsAvailable() &&  (all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_ALL || all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_MALE))
                    {
                        if(height == 2 && width == 1)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_port);
                        }
                        else if(height == 1 && width == 2)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_land);
                        }
                        else
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_available_seat_port);
                        }

                        Seat_view.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {


                                if(v.getTag(R.string.IsSelected).equals("false"))
                                {

                                    selectSeat(v,height,width);

                                }
                                else
                                {

                                    v.setTag(R.string.IsSelected,"false");
                                    if(height == 2 && width ==1)
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_port);
                                    }
                                    else if(height == 1 && width == 2)
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_land);
                                    }
                                    else
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_available_seat_port);
                                    }

                                    My_realm.beginTransaction();
                                    RealmResults<Realm_Selected_Seats> single_row = My_realm.where(Realm_Selected_Seats.class).equalTo("SeatNo",v.getTag(R.string.SeatNo).toString()).findAll();
                                    single_row.deleteAllFromRealm();
                                    My_realm.commitTransaction();

                                    ((Activity_Select_Seats) getActivity()).update_seat_fare();
                                }



                            }
                        });

                        layout.addView(Seat_view);
                    }
                    else if(!all_seats.get(i).getIsAvailable() && (all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_ALL || all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_MALE))
                    {

                        if(height == 2 && width == 1)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_booked_sleeper_seat_port);
                        }
                        else if(height == 1 && width == 2)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_booked_sleeper_seat_land);
                        }
                        else
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_booked_seat_port);
                        }


                        layout.addView(Seat_view);
                    }
                    else if(all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_FEMALE)
                    {
                        if(height == 2 && width == 1)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_port);
                        }
                        else if(height == 1 && width == 2)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_land);
                        }
                        else

                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_seat_port);
                        }

                        Seat_view.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {

                                if(v.getTag(R.string.IsSelected).equals("false"))
                                {

                                    selectSeat(v,height,width);

                                }
                                else
                                {
                                    v.setTag(R.string.IsSelected,"false");
                                    if(height == 2 && width ==1)
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_port);
                                    }
                                    else if(height == 1 && width == 2)
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_land);
                                    }
                                    else
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_ladies_seat_port);
                                    }

                                    My_realm.beginTransaction();
                                    RealmResults<Realm_Selected_Seats> single_row = My_realm.where(Realm_Selected_Seats.class).equalTo("SeatNo",v.getTag(R.string.SeatNo).toString()).findAll();
                                    single_row.deleteAllFromRealm();
                                    My_realm.commitTransaction();

                                    ((Activity_Select_Seats) getActivity()).update_seat_fare();

                                }



                            }
                        });

                        layout.addView(Seat_view);
                    }
                    else if(!all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_FEMALE)
                    {
                        if(height == 2 && width == 1)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_booked_sleeper_seat_port);
                        }
                        else if(height == 1 && width == 2)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_booked_sleeper_seat_land);
                        }
                        else
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_booked_seat_port);
                        }

                        layout.addView(Seat_view);
                    }




        }



        return view;
    }


}
