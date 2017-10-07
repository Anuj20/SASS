package gamer.sass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gamer on 8/26/2017.
 */

public class AboutMe extends AppCompatActivity {

    ExpandableListView about;
    private AboutmeAdapter listAdapter;
    private List<String> ListHeader;
    private HashMap<String, List<String>> ListChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        about= (ExpandableListView) findViewById(R.id.aboutapp);

        termsinfo();
        listAdapter= new AboutmeAdapter(getApplicationContext(), ListHeader, ListChild);
        about.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), Homepage.class));
    }

    public void termsinfo(){
        ListHeader = new ArrayList<String>();
        ListChild = new HashMap<String, List<String>>();

        ListHeader.add("About Developer");
        ListHeader.add("Open source credits");
        ListHeader.add("Privacy policy");

        List<String> about = new ArrayList<String>();
        about.add("Hi all, I am Anuj. I am a 4th year student of electronics and communication branch. I am not the best, but I am passionate about android. We all as college students have so much to sell. So, this summer I decided to make something that would be really helpful to all of us.\n"+
                "Instead of selling our old books and notes to Saluja for cheap rates, now you can just sell your stuff to your fellow colleagues. This App is the result of long hours of coding by me. Currently this app has lots of bugs as I am learning development. I urge you to bear with me and help in the improvement of the app by sending me any and all concerns or bugs you face. This will help me provide you a better app experience.\n" +
                "Send in a polite thanks if this app helps you.");


        List<String> opensource = new ArrayList<String>();
        opensource.add("This application uses open source third party libraries. You can find the source cod of their open source projects along with the license information below. We acknowledge and are grateful to these developers for their contribution to open source.\n" +
                "\n" +
                "Glide\n" +
                "https://github.com/bumptech/glide\n" +
                "Author: Sam Judd - @sjudd on GitHub, @samajudd on Twitter\n" +
                "Copyright 2014 Google, Inc. All rights reserved.\n" +
                "\n" +
                "Volley\n" +
                "https://github.com/google/volley\n" +
                "Author: Google\n"+
                "Copyright 2013 Google, Inc. All rights reserved.\n" +
                "\n"+
                "CircleImageview\n" +
                "https://github.com/hdodenhof/CircleImageView\n"+
                "Author: Henning Dodenhof \n" +
                "Copyright 2014 - 2017 Henning Dodenhof\n"+
                "Firebase\n"+
                "https://github.com/firebase/\n"+
                "Author: Google\n"+
                "Copyright: Google");

        List<String> privacy= new ArrayList<String>();
        privacy.add("\n" +
                "Privacy Policy of SASS\n" +
                "\n" +
                "This Application collects some Personal Data from its Users.\n" +
                "Policy summary\n" +
                "Personal Data collected for the following purposes and using the following services:\n" +
                "\n" +
                "        Device permissions for Personal Data access\n" +
                "            Device permissions for Personal Data access\n" +
                "\n" +
                "            Personal Data: Camera permission\n" +
                "\n" +
                "Full policy\n" +
                "Data Controller and Owner\n" +
                "Types of Data collected\n" +
                "\n" +
                "Among the types of Personal Data that this Application collects, by itself or through third parties, there are: Camera permission.\n" +
                "\n" +
                "Complete details on each type of Personal Data collected are provided in the dedicated sections of this privacy policy or by specific explanation texts displayed prior to the Data collection.\n" +
                "The Personal Data may be freely provided by the User, or, in case of Usage Data, collected automatically when using this Application.\n" +
                "All Data requested by this Application is mandatory and failure to provide this Data may make it impossible for this Application to provide its services. In cases where this Application specifically states that some Data is not mandatory, Users are free not to communicate this Data without any consequences on the availability or the functioning of the service.\n" +
                "Users who are uncertain about which Personal Data is mandatory are welcome to contact the Owner.\n" +
                "Any use of Cookies – or of other tracking tools – by this Application or by the owners of third party services used by this Application serves the purpose of providing the service required by the User, in addition to any other purposes described in the present document and in the Cookie Policy, if available.\n" +
                "\n" +
                "Users are responsible for any third party Personal Data obtained, published or shared through this Application and confirm that they have the third party's consent to provide the Data to the Owner.\n" +
                "Mode and place of processing the Data\n" +
                "Methods of processing\n" +
                "\n" +
                "The Data Controller processes the Data of Users in a proper manner and shall take appropriate security measures to prevent unauthorized access, disclosure, modification, or unauthorized destruction of the Data.\n" +
                "The Data processing is carried out using computers and/or IT enabled tools, following organizational procedures and modes strictly related to the purposes indicated. In addition to the Data Controller, in some cases, the Data may be accessible to certain types of persons in charge, involved with the operation of the site (administration, sales, marketing, legal, system administration) or external parties (such as third party technical service providers, mail carriers, hosting providers, IT companies, communications agencies) appointed, if necessary, as Data Processors by the Owner. The updated list of these parties may be requested from the Data Controller at any time.\n" +
                "Place\n" +
                "\n" +
                "The Data is processed at the Data Controller's operating offices and in any other places where the parties involved with the processing are located. For further information, please contact the Data Controller.\n" +
                "Retention time\n" +
                "\n" +
                "The Data is kept for the time necessary to provide the service requested by the User, or stated by the purposes outlined in this document, and the User can always request that the Data Controller suspend or remove the data.\n" +
                "The use of the collected Data\n" +
                "\n" +
                "The Data concerning the User is collected to allow the Owner to provide its services, as well as for the following purposes: Device permissions for Personal Data access.\n" +
                "\n" +
                "The Personal Data used for each purpose is outlined in the specific sections of this document.\n" +
                "Device permissions for Personal Data access\n" +
                "\n" +
                "Depending on the User's specific device, this Application may request certain permissions that allow it to access the User's device Data as described below.\n" +
                "\n" +
                "By default, these permissions must be granted by the User before the respective information can be accessed. Once the permission has been given, it can be revoked by the User at any time. In order to revoke these permissions, Users may refer to the device settings or contact the Owner for support at the contact details provided in the present document.\n" +
                "The exact procedure for controlling app permissions may be dependent on the User's device and software.\n" +
                "\n" +
                "Please note that the revoking of such permissions might impact the proper functioning of this Application.\n" +
                "\n" +
                "If User grants any of the permissions listed below, the respective Personal Data may be processed (i.e accessed to, modified or removed) by this Application.\n" +
                "Camera permission\n" +
                "\n" +
                "Used for accessing the camera or capturing images and video from the device.\n" +
                "Detailed information on the processing of Personal Data\n" +
                "\n" +
                "Personal Data is collected for the following purposes and using the following services:\n" +
                "\n" +
                "    Device permissions for Personal Data access\n" +
                "\n" +
                "Additional information about Data collection and processing\n" +
                "Legal action\n" +
                "\n" +
                "The User's Personal Data may be used for legal purposes by the Data Controller, in Court or in the stages leading to possible legal action arising from improper use of this Application or the related services.\n" +
                "The User declares to be aware that the Data Controller may be required to reveal personal data upon request of public authorities.\n" +
                "Additional information about User's Personal Data\n" +
                "\n" +
                "In addition to the information contained in this privacy policy, this Application may provide the User with additional and contextual information concerning particular services or the collection and processing of Personal Data upon request.\n" +
                "System logs and maintenance\n" +
                "\n" +
                "For operation and maintenance purposes, this Application and any third party services may collect files that record interaction with this Application (System logs) or use for this purpose other Personal Data (such as IP Address).\n" +
                "Information not contained in this policy\n" +
                "\n" +
                "More details concerning the collection or processing of Personal Data may be requested from the Data Controller at any time. Please see the contact information at the beginning of this document.\n" +
                "The rights of Users\n" +
                "\n" +
                "Users have the right, at any time, to know whether their Personal Data has been stored and can consult the Data Controller to learn about their contents and origin, to verify their accuracy or to ask for them to be supplemented, cancelled, updated or corrected, or for their transformation into anonymous format or to block any data held in violation of the law, as well as to oppose their treatment for any and all legitimate reasons. Requests should be sent to the Data Controller at the contact information set out above.\n" +
                "\n" +
                "This Application does not support “Do Not Track” requests.\n" +
                "To determine whether any of the third party services it uses honor the “Do Not Track” requests, please read their privacy policies.\n" +
                "Changes to this privacy policy\n" +
                "\n" +
                "The Data Controller reserves the right to make changes to this privacy policy at any time by giving notice to its Users on this page. It is strongly recommended to check this page often, referring to the date of the last modification listed at the bottom. If a User objects to any of the changes to the Policy, the User must cease using this Application and can request that the Data Controller remove the Personal Data. Unless stated otherwise, the then-current privacy policy applies to all Personal Data the Data Controller has about Users.\n" +
                "Information about this privacy policy\n" +
                "\n" +
                "The Data Controller is responsible for this privacy policy, prepared starting from the modules provided by Iubenda and hosted on Iubenda's servers.\n");

        ListChild.put(ListHeader.get(0), about);
        ListChild.put(ListHeader.get(1),opensource);
        ListChild.put(ListHeader.get(2), privacy);

    }
}
