# Rozgaar
App done for internship with a startup in IITH 
<p>
Structure of the app is as follows:
<ul>
  <li>LanguageSelectionActivity(Rozgaar/app/src/main/java/com/asm/rozgaar/Activities/) is the launcher activity for first time login</li>
  <li>If we are already logged in(which you will be,bcoz I have not given a sign out option yet),you will be directed to <b>HomeScreenFragment</b> of <b>MainActivity</b></li>
  <li>After selecting language, we are redirected to OTP login page(Firebase Auth UI, no specific activities for this process)</li>
  <li>After first time OTP auth verification sucessfull, it takes us to <b>UserTypeFragment</b> of <b>RegisterActivity</b>,where we choose whether we are job seeker or provider</li>
  <li>Clicking either button leads to the respective registration fragments(<b>SeekerDetailsFragment/ProviderDetailsFragment</b>)
</ul>
</p>
<p>
All field data of users have been stored in Firebase Firestore,while the images of their Aadhar,PAN and License have been stored in Firebase Cloud Storage.<br>
  The schema for Firebase Firestore is as follows:
  <ul>
    <li>
      <h3>Job Seekers</h3>
      <ul>
        <li>
          <h4>FirebaseAuth.user.uid</h4>
          <ul>
            <li>name</li>
            <li>phone number</li>
            <li>Gender</li>
            <li>Domain of employment</li>
            <li>Date of Birth</li>
            <li>List of interests</li>
            <li>List of known languages</li>
            <li>isAadharUploaded</li>
            <li>isPanUploaded</li>
            <li>isLicenseUploaded</li>
            <li>Address</li>
          </ul> 
        </li>
      </ul>
    </li>
    <li><h3>Job Providers</h3>
      <ul>
      <li><h4>FirebaseAuth.user.uid</h4>
        <ul>
          <li><h5>personalDetails</h5>
            <ul>
              <li>Name</li>
              <li>Phone Number</li>
              <li>Address</li>
              <li>Email ID</li>
              <li>Aadhar Image URI</li>
            </ul>
          </li>
          <li>
            <h5>Office Details</h5>
            <ul>
              <li>Company Name</li>
              <li>CIN NUmber</li>
              <li>Company Category</li>
              <li>Office Address</li>
            </ul>
          </li>
          <li><h5>Job Details</h5>
            <ul>
              <li>Job Category</li>
              <li>Job Description</li>
              <li>Salary</li>
              <li>Deadline</li>
              <li>Job Vacancies</li>
              <li>Job Requirements</li>
              <li><h6>Location</h6>
                <ul>
                  <li>State</li>
                  <li>City</li>
                  <li>Area</li>
                </ul>
              </li>
            </ul>
          </li>
        </ul>  
      </li>
      </ul>
    </li>
</ul>
</p>
