package com.example.app.krishishop;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    FloatingActionButton action_bot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help, container,  false);

        action_bot = view.findViewById(R.id.action_bot);

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        // preparing list data
        prepareListData();

//        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        listAdapter = (ExpandableListAdapter) new com.example.app.krishishop.adapter.ExpandableListAdapter(getActivity(),listDataHeader,listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                return false;
            }
        });

        action_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChatBotActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setContentView(int fragment_help) {
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("What is Farming ?");
        listDataHeader.add("Which are the top 10 agricultural producing states ?");
        listDataHeader.add("Steps to test soil using jar ?");
        listDataHeader.add("How important is agriculture to the overall economy?");
        listDataHeader.add("What impact does farming have on soil health?");
        listDataHeader.add("What are pesticides?");
        listDataHeader.add("What are the different farming systems?");
        listDataHeader.add("What factors affect crop production?\n");
        listDataHeader.add("What is the role of irrigation in farming?");
        listDataHeader.add("How can technology assist farmers?");

        // Adding child data
        List<String> Q1 = new ArrayList<String>();
        Q1.add("Farming is the act or process of working the ground,planting seeds, and growing edible plants.");

        List<String> Q2 = new ArrayList<String>();
        Q2.add("1.California.");
        Q2.add("2.Iowa.");
        Q2.add("3.Texas.");
        Q2.add("4.Nebraska.");
        Q2.add("5.Minnesota.");
        Q2.add("6.Illinois.");
        Q2.add("7.Kansas.");
        Q2.add("8.North Carolina.");
        Q2.add("9.Wisconsin.");
        Q2.add("10.Indiana.");


        List<String> Q3 = new ArrayList<String>();
        Q3.add("1. Set the jar down and look at your watch. In 1 minute, measure (with the ruler) the amount of sediment that has collected at the bottom. This is the sand in your soil.");
        Q3.add("2. Wait 4 minutes more. Measure the sediment again: The difference between the two numbers will be the amount of silt in your soil.");
        Q3.add("3. Take a third measurement in 24 hours. The difference between the second and third number will be the amount of clay in your soil.");
        Q3.add("-> Calculate the percentages of sand, silt, and clay, which should add up to 100 percent. Healthy soil typically consists of 20 percent clay, 40 percent silt, and 40 percent sand.");
        Q3.add("-> If you have sandy soil, add humus or aged manure, peat moss, or sawdust with some extra nitrogen. Heavy, clay-rich soil can also be added to improve the soil.");
        Q3.add("-> If you have silty soil, add coarse sand (not beach sand) or gravel and compost, or well-rotted horse manure mixed with fresh straw.");
        Q3.add("-> If you have clay soil, add coarse sand (not beach sand), compost, and peat moss.");
        Q3.add("4. The pantry test PH test for soil acidity and alkalinity.");
        Q3.add("-> Place 2 tablespoons of soil in a bowl and add ½ cup vinegar.If the mixture fizzes, you have alkaline soil.");
        Q3.add("-> If it does not react to either test, the soil has a neutral pH.");
        Q3.add("-> A very high or very low soil pH may result in plant nutrient deficiency or toxicity.");
        Q3.add("-> A pH value of 7 is neutral; microbial activity is greatest and plant roots absorb/access nutrients best when the pH is in the 5.5 to 7 range.");
        Q3.add("-> Once you figure out your soil pH, you can change or adjust it. Acidic (sour) soil is counteracted by applying finely ground limestone, and alkaline (sweet) soil is treated with ground sulfur.");

        List<String> Q4 = new ArrayList<String>();
        Q4.add("It is estimated that India’s agriculture sector accounts only for around 14 percent of the country’s economy but for 42 percent of total employment. As around 55 percent of India’s arable land depends on precipitation, the amount of rainfall during the monsoon season is very important for economic activity.");

        List<String> Q5 = new ArrayList<String>();
        Q5.add("There are three main ways farming affects soil health:\n" +
                "\n" +
                " Adding nutrients. Farmers often add nutrients to the soil to improve crop yields. However, some of these nutrients and fertilizers can affect the micro-organisms in the soil’s ability to produce natural compounds the crops need. \n" +
                " Soil Loss. While soil loss has improved greatly since the 1980s, farming still leads to loss of soil, due to processes like clearing trees and plowing. These losses are roughly 5 tons of soil for each ton of grain planted.  \n" +
                " Overgrazing. The last way farming impacts soil is through overgrazing, which is when animals eat large amounts of the natural vegetation, causing the soil to wash or blow away. Extreme overgrazing can even lead to desertification or expansion of desert conditions, once the natural plant cover has been eliminated. ");

        List<String> Q6 = new ArrayList<String>();
        Q6.add("Pesticides are any substances used to keep various harmful plants and animals away from your crops.  There are six main types of pesticides: \n" +
                "\n" +
                "Insecticides. These chemicals specifically kill or repel insects. \n" +
                "Herbicides. These chemicals kill or repel weeds and other harmful plants. \n" +
                "Rodenticides. These chemicals kill or repel rodents. \n" +
                "Bactericides. These chemicals kill or repel various types of harmful bacteria. \n" +
                "Fungicides. These chemicals kill or repel various types of fungi. \n" +
                "Larvicides. These chemicals kill or repel various types of larvae.");

        List<String> Q7 = new ArrayList<String>();
        Q7.add("There are many different ways to farm. \n" +
                "\n" +
                "These farming systems tend to arise out of necessity born from the farming environment, such as water, land, grazing areas, climate, etc. \n" +
                "\n" +
                "Here are some of the most popular farming systems: \n" +
                "\n" +
                "Arable Farming. With arable farming, the farmer plants only crops like vegetables, grains, or legumes. If you are a “do it yourself” (DIY) at-home farmer, chances are you practice arable farming. \n" +
                "Mixed Farming. Mixed farming mixes the planting of crops with the raising of animals on the same piece of land. Most traditional farms practice mixed farming.  \n" +
                "Subsistence Farming. Subsistence farming is done to provide food for the farmer and his family. Subsistence farming is generally DIY and produces low yields.  \n" +
                "Shifting Cultivation. One of the most controversial farming systems, shifting cultivation occurs when a farmer clears a piece of land, uses it for 3-5 years, then abandons it when it has been used up. Many governments discourage this type of farming as it is not very sustainable. \n" +
                "Plantation Farming. One of the oldest farming systems. Plantation farming uses a large piece of land to grow one specific crop, like cotton or tea. Plantation farming is also sometimes referred to as tree crop farming.  \n" +
                "Livestock Farming. Similar to arable farming, livestock farming focuses solely on raising animals, not planting crops. This is another farming system that is not very sustainable because the animals’ grazing eventually destroys the land.  \n" +
                "Nomadic Farming. Similar to livestock farming. In nomadic farming, the farmer moves his or her animals around in search of fresh grazing and water. ");


        List<String> Q8 = new ArrayList<String>();
        Q8.add(" There are four major factors that affect crop production during the farming process: \n" +
                "\n" +
                "Nutrients. There are 18 different nutrients required for crop production. Nutrients have a direct impact on things like protein production, photosynthesis, and root, shoot, and leaf development. \n" +
                "Water Availability. The amount of water your crops receive plays a direct role in the amount of crops you produce. Too little water and your crops will shrivel up and die. Too much water and you end up with wasted water, energy, and labor. \n" +
                "Climate. The climate your crops are planted in heavily influences crop production. Factors such as humidity, temperature, and wind can wreak havoc on your crops. One of the major benefits of farming in a controlled environment, like a shipping container or greenhouse, is it allows you to control the climate 24/7.  \n" +
                "Pests and diseases. Pests and diseases damage your crops directly by eating or destroying the plants themselves. But pests also destroy your crops in less obvious ways like damaging plant roots, altering the ability of the plant to absorb nutrients.  ");


        List<String> Q9 = new ArrayList<String>();
        Q9.add("Irrigation is the process of getting the right amount of water to plants at the right time. Irrigation is arguably the largest breakthrough in farming history as it laid the base for economies and societies across the world. \n" +
                "\n" +
                "Among the main benefits of irrigation are crop production, landscape maintenance, frost protection, weed suppression, and disposal of sewage. \n" +
                "\n" +
                "Simply put, farming could not occur without irrigation. ");

        List<String> Q10 = new ArrayList<String>();
        Q10.add("There are five major ways technology has affected farming: \n" +
                "\n" +
                "Livestock genetics and breeding. Humans have been attempting to breed healthier, more productive animals for more than 10,000 years. Breakthroughs in technology, like artificial insemination and gene editing, have allowed humans to improve livestock genetics.\n" +
                "Crop genetics. Just like humans have been experimenting with breeding superior animals for thousands of years, so too have humans attempted to breed more robust resilient plants. Technological advances, like marker assisted breeding, have allowed farmers to increase crop yields across the world. \n" +
                "Labor and automation. When you think about how farming has been affected by technology, you probably think about machinery. From the cotton gin to the tractor, improvements in farming technology have led directly to more food being produced with less labor. \n" +
                "Livestock facilities. Just a few decades ago, farmers had to keep all of their livestock outside, at the mercy of the elements, thieves, and natural predators. But over the past few years, there has been a dramatic rise in the use of climate-controlled barns. These barns have WI-FI, automatic feed systems, and more. Farmers can monitor these smart barns from their cellphones and even make adjustments to the conditions the livestock is in. All without having to set foot in the barn. \n" +
                "Specialization. Finally, more advanced technology has brought a specialized approach to farming. Gone are the days of growing everything on the same farm, instead today’s farmers use specific technology to perfect a few types of crops. You will also see farms honing in on specific niches, like Pure Greens does with container farming or Queen Creek Olive Mill does with olives. ");

        listDataChild.put(listDataHeader.get(0),Q1); // Header, Child data
        listDataChild.put(listDataHeader.get(1),Q2);
        listDataChild.put(listDataHeader.get(2),Q3);
        listDataChild.put(listDataHeader.get(3),Q4);
        listDataChild.put(listDataHeader.get(4),Q5);
        listDataChild.put(listDataHeader.get(5),Q6);
        listDataChild.put(listDataHeader.get(6),Q7);
        listDataChild.put(listDataHeader.get(7),Q8);
        listDataChild.put(listDataHeader.get(8),Q9);
        listDataChild.put(listDataHeader.get(9),Q10);
    }

}
