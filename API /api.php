<?php

/**
* This is the REST API for the RSVs App.
* It supports various CRUD Requests and algorithms for Car Rentals Reservations/Bookings Management.
* 
* @author: Ioannis Brant Ioannidis <giannisbrant@gmail.com>
*/

//Connect to the database.
require_once("connect.php");

//---------------------------------------------------------------RESERVATIONS REQUESTS 
  //route("/reservations")
  if ($_GET['url'] == "reservations") {
        $sql = "SELECT reservations.*,clients.name as client_name,cars.name as car_name FROM reservations LEFT JOIN clients ON client_id=clients.id LEFT JOIN cars ON car_id=cars.id ORDER BY day_start ASC";

       $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
          echo $json;
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        }  
  }
  //route("/returnreservation")
  elseif($_GET['url'] == "returnreservation"){
	  $id = $_GET['id'];
	  
	  $sql = "SELECT reservations.*,clients.name as client_name,cars.name as car_name FROM reservations LEFT JOIN clients ON client_id=clients.id LEFT JOIN cars ON car_id=cars.id WHERE reservations.id=$id";

       $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
          echo $json;
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        }  
  }
  //route("/returnAvailableCars")
  if ($_GET['url'] == "returnAvailableCars") {
        $rsvsSql = "SELECT * FROM reservations";
	  	$carsSql = "SELECT * FROM cars";
	  
	  	//Reservation to be added
	  	$day_start = $_GET['day_start'];
	    $arr_day_start = explode("-",$day_start);
	    $start_month = $arr_day_start[1];
	  	$hour_start = $_GET['hour_start'];
	  	$day_end = $_GET['day_end'];
	  	$hour_end = $_GET['hour_end'];
	  
        $resultRsv = $mysqli->query($rsvsSql);
	  	$resultCar = $mysqli->query($carsSql);
        if ($resultRsv->num_rows == 0){
		  //There are no reservations in the database.
		  //In this case the reservation can be added for any car that the user will choose within the application.
          while($row = mysqli_fetch_assoc($resultCar)) {
            if (!is_null($row)){
              $cars[] = $row;
            }
          }
			$i=0;
		  foreach($cars as $car){
			  $car_id = $car['car_id'];
			  $monthlyRsvs = "SELECT COUNT(*) as monthly_rsvs FROM reservations WHERE MONTH('$day_start')='$start_month' AND car_id=$car_id";
			  $cars[$i]['monthly_rsvs'] = $monthly_rsvs;
			  $i++;
		  }
		  echo json_encode($cars); //all cars are available, so return all of them.
        }
	  //IN CASE THERE ARE RESERVATIONS IN THE DATABASE.
	  else{
		//store all cars into an array.
		while($row = mysqli_fetch_assoc($resultCar)) {
            if (!is_null($row)){
              $cars[] = $row;
            }
          }
		//check which cars are available to be booked for the given dates.
		$i=0;
		foreach($cars as $car){
			if(carIsAvailable($car['id'],$day_start,$hour_start,$day_end,$hour_end)){
			   $availableCars[$i]=$car;
				$carId = $car['id'];
			   //Query to find the total number of reservations for the starting month and for the current car.
			   $monthlyRsvs = "SELECT COUNT(*) as monthly_rsvs FROM reservations WHERE MONTH('$day_start')='$start_month' AND car_id=$carId";
			   $resultMonthlyRsvs = $mysqli->query($monthlyRsvs);
			   $monthly_rsvs = mysqli_fetch_assoc($resultMonthlyRsvs);
			   $availableCars[$i]['monthly_rsvs'] = $monthly_rsvs['monthly_rsvs'];
			   $i++;
			}
		}
		  usort($availableCars, "method1"); //sort reservations based on total number of monthly reservations.
		  echo json_encode($availableCars); //json response for the available cars to be booked for the specific dates.
		$i=0;  
	  }
 		
  }
  //route("/addReservation")
  elseif($_GET['url'] == "addReservation"){
	  //The process has passed the stage of checking available cars.This function just adds a reservation into the database.
	  //It also adds the client that booked the reservation in the clients' database, and updates the total reservations of the
	  //given car.
	  $id = rand(100000,999999);
	  $car_id = $_POST['car_id'];
	  $client_id = rand(1000,9999) + rand(1,99);
	  $client_name = $_POST['client_name'];
	  $client_email = $_POST['client_email'];
	  $client_telephone = $_POST['client_telephone'];
	  $client_nationality = $_POST['client_nationality'];
	  $day_start = $_POST['day_start'];
	  $hour_start = $_POST['hour_start'];
	  $day_end = $_POST['day_end'];
	  $hour_end = $_POST['hour_end'];
	  $total_money = $_POST['total_money'];
	  
	  //check if the given client already exists.
	  $sql_check_client = "SELECT * FROM clients WHERE name='$client_name'";
	  $result1 = $mysqli->query($sql_check_client);
	  
	  if ($result1->num_rows == 0){ //this client is a new one, or has been added before under a different name.
		  //add client into database.
          $sql_client = "INSERT INTO clients (id, name,email,telephone,nationality)
		  				VALUES ($client_id,'$client_name','$client_email','$client_telephone','$client_nationality')"; 
	  	  $result2 = $mysqli->query($sql_client); 
        }elseif ($result1->num_rows > 0){
          //Client is already present in the database.Assign the rsv's client_id to the existed client id.
		  while($row = mysqli_fetch_assoc($result1)) {
            if (!is_null($row)){
              $client[] = $row;
			  $client_id = $row['id'];
            }
          }
        }
	  
	  $sql = "INSERT INTO reservations (id, car_id, client_id,day_start,hour_start,day_end,hour_end,total_money)
VALUES ($id, $car_id, $client_id,'$day_start','$hour_start','$day_end','$hour_end','$total_money')";
	  
	  $result=$mysqli->query($sql);
	  
  }
  //route("/editReservation")
  elseif($_GET['url'] == "editReservation"){
	  $id = $_POST['id'];
	  $client_id = $_POST['client_id'];
	  $car_id = $_POST['car_id'];
	  $client_name = $_POST['client_name'];
	  $client_email = $_POST['client_email'];
	  $client_telephone = $_POST['client_telephone'];
	  $client_nationality = $_POST['client_nationality'];
	  $day_start = $_POST['day_start'];
	  $hour_start = $_POST['hour_start'];
	  $day_end = $_POST['day_end'];
	  $hour_end = $_POST['hour_end'];
	  $total_money = $_POST['total_money'];
	  
	  $sql_update_rsv = "UPDATE reservations SET car_id=$car_id,client_id=$client_id,day_start='$day_start'  ,hour_start='$hour_start',day_end='$day_end',hour_end='$hour_end',total_money='$total_money' WHERE id=$id";
	  $result = $mysqli->query($sql_update_rsv);
	  
	  $sql_update_client = "UPDATE clients SET name='$client_name',email='$client_email',telephone='$client_telephone',nationality='$client_nationality' WHERE id=$client_id";
	  $result2 = $mysqli->query($sql_update_client);
  }
  //route("/archiveReservation")
  elseif($_GET['url'] == "archiveReservation"){
	  $id = $_POST['id'];
	  $car_id = $_POST['car_id'];
	  $client_id = $_POST['client_id'];
	  $day_start = $_POST['day_start'];
	  $hour_start = $_POST['hour_start'];
	  $day_end = $_POST['day_end'];
	  $hour_end = $_POST['hour_end'];
	  $total_money = $_POST['total_money'];
	  
	  //insert reservation into archived reservations.
	   $sql = "INSERT INTO reservations (id, car_id, client_id,day_start,hour_start,day_end,hour_end,total_money)
		VALUES ($id, $car_id, $client_id,'$day_start','$hour_start','$day_end','$hour_end','$total_money')";
	  $result = $mysqli->query($sql);
	  
	  //delete reservation from reservations table.
	  $sql_delete_reservation = "DELETE FROM reservations WHERE id=$id";
	  $result_delete = $mysqli->query($sql_delete_reservation);
  }
  //route("/todayReservations")
  elseif($_GET['url'] == "todayReservations"){
	  $date = date("Y-m-d");
	  $sql = "SELECT reservations.*,clients.name as client_name,cars.name as car_name FROM reservations LEFT JOIN clients ON client_id=clients.id LEFT JOIN cars ON car_id=cars.id WHERE day_start='$date' OR day_end='$date' ORDER BY id";
	  
	  $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
          echo $json;
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        }
  }
  //route("/tommorowReservations")
  elseif($_GET['url'] == "tomorrowReservations"){
	  $date = date("Y-m-d");
	  $next_date = date('Y-m-d', strtotime($date .' +1 day'));
	  $sql = "SELECT reservations.*,clients.name as client_name,cars.name as car_name FROM reservations LEFT JOIN clients ON client_id=clients.id LEFT JOIN cars ON car_id=cars.id WHERE day_start='$next_date' OR day_end='$next_date' ORDER BY id";
	  
	  $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
          echo $json;
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        }
  }
  //route("/reservationsByCar")
  elseif($_GET['url'] == "reservationsByCar"){
	  $month = $_GET['month'];
	  //fetch all car ids that exist in the database.
	  $sql_cars = "SELECT * FROM cars";
	  $result = $mysqli->query($sql_cars);
      if ($result->num_rows == 0){
          $jsonString = '0';
      }elseif ($result->num_rows > 0){
         while($row = mysqli_fetch_assoc($result)) {
           if (!is_null($row)){
             $cars[] = $row;
           }
         }
      }
	  //Fetch and return all reservations that have been made in every car for a given month.
	  foreach($cars as $car){
		  	$car_id = $car['id'];
			$sql = "SELECT reservations.*,clients.name as client_name,cars.name as car_name FROM reservations LEFT JOIN clients ON client_id=clients.id LEFT JOIN cars ON car_id=cars.id WHERE car_id=$car_id AND MONTH(day_start)='$month'";
		  	$result_rsvs = $mysqli->query($sql);
		  	while($row = mysqli_fetch_assoc($result_rsvs)) {
           		if (!is_null($row)){
             		$json[] = $row;
           		}
         	}
	  }
	   echo json_encode($json);
  }
//-------------------------------------------------------------------------------------------------CAR REQUESTS
  //route("/cars")
  elseif($_GET['url'] == "cars"){
	  //returns all cars that have been added to the database for a given category.
	  $category = $_GET['category'];
	  $sql = "SELECT * FROM cars WHERE category='$category'";
	  if($category == ""){
		  $sql = "SELECT * FROM cars ORDER BY category"; //return cars from all categories.
	  }
       $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
          echo $json;
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        }  
  }
  //route("/returncar")
  elseif($_GET['url'] == "returncar"){
	  $id = $_GET['id'];
	  $sql = "SELECT * FROM cars WHERE id=$id";
       $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
          echo $json;
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        } 
  }
  //route("/addcar")
  elseif($_GET['url'] == "addcar"){
	  $sql_rowcount = "SELECT * FROM cars";
	  $num = $mysqli->query($sql_rowcount);
	  $row_number = $num->num_rows;
	  $id = $row_number + 1;
	  
	  $name = $_POST['name'];
	  $category = $_POST['category'];
	  $kms = $_POST['kms'];
	  $license_plate = $_POST['license_plate'];
	  $total_rsvs = $_POST['total_rsvs'];
	  
	  $sql = "INSERT INTO cars (id,name,category,kms,license_plate,total_rsvs) 
	  VALUES ($id,'$name','$category','$kms','$license_plate','$total_rsvs')";
	  
	  $result = $mysqli->query($sql);
  }
  //route("/editcar")
  elseif($_GET['url']=="editcar"){
	  $id = $_POST['id'];
	  $name = $_POST['name'];
	  $category = $_POST['category'];
	  $kms = $_POST['kms'];
	  $license_plate = $_POST['license_plate'];
	  $total_rsvs = $_POST['total_rsvs'];
	  
	  $sql = "UPDATE cars SET id=$id,name='$name',category='$category',kms='$kms',license_plate='$license_plate',total_rsvs='$total_rsvs'";
	  
	  $result = $mysqli->query($sql);
  }
  //route("/deletecar")
  elseif($_GET['url']=="deletecar"){
	  $id = $_POST['id'];
	  $sql = "DELETE FROM cars WHERE id=$id";
	  
	  $result = $mysqli->query($sql);
  }
//-------------------------------------------------------------------------------------------------CLIENT REQUESTS
  //route("/clients")
  elseif($_GET['url'] == "clients"){
	  $sql = "SELECT * FROM clients";
       $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
          echo $json;
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        } 
  }
  //route("/getclient")
  elseif($_GET['url'] == "getclient"){
	  $client_id = $_GET['client_id'];
	  $sql = "SELECT * FROM clients WHERE id=$client_id";
	  $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
          echo $json;
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        } 
  }
  //route("/editclient")
  elseif($_GET['url'] == "editclient"){
	  $id = $_POST['client_id'];
	  $name = $_POST['client_name'];
	  $email = $_POST['client_email'];
	  $telephone = $_POST['client_telephone'];
	  $nationality = $_POST['client_nationality'];
	  
	  $sql_edit_client = "UPDATE clients SET name='$name',email='$email',telephone='$telephone',nationality='$nationality'";
	  $result = $mysqli->query($sql_edit_client);
  }
  //route("/deleteclient")
  elseif($_GET['url'] == "deleteclient"){
	  $id = $_GET['client_id'];
	  $sql_delete_client = "DELETE FROM clients WHERE id=$id";
	  $result = $mysqli->query($sql_delete_client);
  }
//-------------------------------------------------------------------------------------------------SINGLE CAR STATISTICS
  //route("/singleCarStats")
  elseif($_GET['url'] == "singleCarStats"){
	  $car_id = $_GET['id'];
	  $date = date("Y-m-d");
	  $before_one_year_date = date('Y-m-d', strtotime($date .' -365 days'));
	  $before_one_month_date = date('Y-m-d', strtotime($date .' -31 days'));
	  
	  //Yearly reservations number of the selected car.
	  $sql_yearly_rsvs = "SELECT COUNT(*) as yearly_rsvs FROM archived_reservations WHERE day_start>='$before_one_year_date' AND day_end<='$date' AND car_id=$car_id";
	  $result_yearly_rsvs = $mysqli->query($sql_yearly_rsvs);
	  if ($result_yearly_rsvs->num_rows == 0){
          $yearly_rsvs = '0';
        }elseif ($result_yearly_rsvs->num_rows > 0){
          while($row = mysqli_fetch_assoc($result_yearly_rsvs)) {
            if (!is_null($row)){
              $yearly_rsvs = $row['yearly_rsvs'];
            }
          }
        }
	  
	  //Monthly reservations number of the selected car.
	  $sql_monthly_rsvs = "SELECT COUNT(*) as monthly_rsvs FROM archived_reservations WHERE day_start>='$before_one_month_date' AND day_end<='$date' AND car_id=$car_id";
	  $result_monthly_rsvs = $mysqli->query($sql_monthly_rsvs);
	  if ($result_monthly_rsvs->num_rows == 0){
          $monthly_rsvs = '0';
        }elseif ($result_monthly_rsvs->num_rows > 0){
          while($row = mysqli_fetch_assoc($result_monthly_rsvs)) {
            if (!is_null($row)){
              $monthly_rsvs = $row['monthly_rsvs'];
            }
          }
        }
	  
	  //Most clients' percentage by nationality for this car and all historical reservations.
	  //Fetch total reservations of this car:
	  $sql_total_rsvs = "SELECT COUNT(*) as total_rsvs FROM archived_reservations WHERE car_id=$car_id";
	  $result_total_rsvs = $mysqli->query($sql_total_rsvs);
	  if ($result_total_rsvs->num_rows == 0){
          $total_rsvs = '0';
        }elseif ($result_total_rsvs->num_rows > 0){
          while($row = mysqli_fetch_assoc($result_total_rsvs)) {
            if (!is_null($row)){
              $total_rsvs = $row['total_rsvs'];
            }
          }
        }
	    //Fetch nationality of clients that made the most reservations.
	  $sql_max_nationality = "SELECT COUNT(*) as num_nationality,clients.nationality FROM reservations LEFT JOIN clients ON clients.id=client_id WHERE car_id=$car_id GROUP BY clients.nationality ORDER BY num_nationality DESC LIMIT 1";
	  $result_max_nationality = $mysqli->query($sql_max_nationality);
	  if ($result_max_nationality->num_rows == 0){
          $max_nationality = '0';
		  $nam_nationality = "Nothing to show.";
        }elseif ($result_max_nationality->num_rows > 0){
          while($row = mysqli_fetch_assoc($result_max_nationality)) {
            if (!is_null($row)){
              $max_nationality[] = $row;
            }
          }
		  $num_nationality = $max_nationality[0]['num_nationality'];
		  $nam_nationality = $max_nationality[0]['nationality'];
        }
	  if($total_rsvs!=0){
	  	$nationality_percentage = ($num_nationality/$total_rsvs)*100;
	  }
	  else $nationality_percentage=0;
	  
	  //Previous service.
	  $date = date('Y-m-d');
	  $sql_previous_service = "SELECT * FROM service WHERE date<'$date' AND car_id=$car_id ORDER BY date DESC LIMIT 1";
	  $result_previous_service = $mysqli->query($sql_previous_service);
	  if ($result_previous_service->num_rows == 0){
          $previous_service_date = 'unset';
        }elseif ($result_previous_service->num_rows > 0){
          while($row = mysqli_fetch_assoc($result_previous_service)) {
            if (!is_null($row)){
              $previous_service_date = $row['date'];
            }
          }
        }
	  
	  //Next upcoming service.
	  $sql_upcoming_service = "SELECT * FROM service WHERE date>'$date' AND car_id=$car_id ORDER BY date ASC LIMIT 1";
	  $result_upcoming_service = $mysqli->query($sql_upcoming_service);
	  if ($result_upcoming_service->num_rows == 0){
          $upcoming_service_date = 'unset';
        }elseif ($result_upcoming_service->num_rows > 0){
          while($row = mysqli_fetch_assoc($result_upcoming_service)) {
            if (!is_null($row)){
              $upcoming_service_date = $row['date'];
            }
          }
        }
	  
	  //Return all data in json form.
	  $sql = "SELECT * FROM cars WHERE id=$car_id";
	  $result = $mysqli->query($sql);
        if ($result->num_rows == 0){
          $jsonString = '0';
          $json = prettyJSON($jsonString);
        }elseif ($result->num_rows > 0){
          while($row = mysqli_fetch_assoc($result)) {
            if (!is_null($row)){
              $json[] = $row;
            }
          }
		  $json[0]['monthly_rsvs'] = $monthly_rsvs;
		  $json[0]['yearly_rsvs'] = $yearly_rsvs;
		  $json[0]['nationality_percentage'] = $nationality_percentage;
		  $json[0]['nam_nationality'] = $nam_nationality;
		  $json[0]['previous_service_date'] = $previous_service_date;
		  $json[0]['upcoming_service_date'] = $upcoming_service_date;
          echo json_encode($json);
        }
        else{
          echo http_response_code(500);
        } 
  }
//-------------------------------------------------------------------------------------------------CAR SERVICE
  //route("/carService")
  elseif($_GET['url'] == "carService"){
	  //Returns the previous and the upcoming service of a car.
	  $car_id = $_GET['id'];
	   //Last time this car has been serviced.
	  $date = date('Y-m-d');
	  $sql_previous_service = "SELECT * FROM service WHERE date<'$date' AND car_id=$car_id ORDER BY date DESC LIMIT 1";
	  $result_previous_service = $mysqli->query($sql_previous_service);
	  if ($result_previous_service->num_rows == 0){
          $previous_service_date = 'unset';
        }elseif ($result_previous_service->num_rows > 0){
          while($row = mysqli_fetch_assoc($result_previous_service)) {
            if (!is_null($row)){
              $previous_service_date = $row['date'];
            }
          }
        }
	  
	  //Next upcoming service.
	  $sql_upcoming_service = "SELECT * FROM service WHERE date>'$date' AND car_id=$car_id ORDER BY date ASC LIMIT 1";
	  $result_upcoming_service = $mysqli->query($sql_upcoming_service);
	  if ($result_upcoming_service->num_rows == 0){
          $upcoming_service_date = 'unset';
        }elseif ($result_upcoming_service->num_rows > 0){
          while($row = mysqli_fetch_assoc($result_upcoming_service)) {
            if (!is_null($row)){
              $upcoming_service_date = $row['date'];
            }
          }
        }
	  
	 //Return service info along with required car info.
	 $sql_service_info = "SELECT name,category,license_plate FROM cars WHERE id=$car_id";
	 $result_service_info = $mysqli->query($sql_service_info);
	 if ($result_service_info->num_rows == 0){
          $json='0';
      }elseif ($result_service_info->num_rows > 0){
        while($row = mysqli_fetch_assoc($result_service_info)) {
         if (!is_null($row)){
           $json[] = $row;
         }
       }
	 }
	 $json[0]['previous_service'] = $previous_service_date;
	 $json[0]['next_service'] = $upcoming_service_date; 
	 echo json_encode($json);
  }
  //route("/scheduleService")
  elseif($_GET['url'] == "scheduleService"){
	  $id = rand(1,120) + rand(1,310);
	  $car_id = $_POST['car_id'];
	  $date = $_POST['date'];
	  
	  //Delete the previous upcoming service from the database.
	  $date = date('Y-m-d');
	  $sql_delete_prev_upcoming = "DELETE FROM service WHERE date>$date AND car_id=$car_id";
	  $result_delete_prev_upcoming = $mysqli->query($sql_delete_prev_upcoming);
	  
	  //Insert new upcoming service.
	  $sql_schedule_service = "INSERT INTO service (id,car_id,date) VALUES ($id,$car_id,'$date')";
	  $result_schedule_service = $mysqli->query($sql_schedule_service);
  }
//-----------------------------------------------------------------------------------------------------NOTIFICATIONS
  //route("/notify")
  elseif($_GET['url'] == "notify"){
	$user_id = $_POST['userid'];$user_id=0;
	$type = $_POST['type'];
	$text_message = $_POST['message'];
	$date = date('Y-m-d');
	$device_id = $_POST['device_id'];
	  
	$tomorrow = date('Y-m-d', strtotime($date .' +1 day'));
	$after_one_week = date('Y-m-d', strtotime($date .' +7 days'));
	
	  
	 
	$sql_check_upcoming_rsvs = "SELECT * FROM reservations WHERE day_start='$tomorrow'";
    $result_check_upcoming_rsvs = $mysqli->query($sql_check_upcoming_rsvs);
	if($result_check_upcoming_rsvs->num_rows>0){
		notify($user_id,"UPCOMING RESERVATION(s)","You have reservation(s) that start tomorrow.",$date,$device_id);
		$sql_insert_notif = "INSERT INTO notifications (userid,type,message,date,created_date) VALUES ($user_id,'$type','$text_message','$tomorrow','$date'";
		$result_insert_notif = $mysqli->query($sql_insert_notif);
	}
	  
	$sql_check_ending_rsvs = "SELECT * FROM reservations WHERE day_end='$tomorrow'";
	$result_check_ending_rsvs = $mysqli->query($sql_check_ending_rsvs);
	if($result_check_ending_rsvs->num_rows>0){
		notify($user_id,"ENDING RESERVATION(s)","You have reservation(s) that end tomorrow.",$date,$device_id);
		$sql_insert_notif = "INSERT INTO notifications (userid,type,message,date,created_date) VALUES ($user_id,'$type','$text_message','$tomorrow','$date'";
		$result_insert_notif = $mysqli->query($sql_insert_notif);
	}
	  
	$sql_check_upcoming_service = "SELECT * FROM service WHERE date='$after_one_week'";
	$result_check_upcoming_service = $mysqli->query($sql_check_upcoming_service);
	if($result_check_upcoming_service->num_rows>0){
		notify($user_id,"UPCOMING SERVICE","You have an upcoming car service in 7 days.",$date,$device_id);
		$sql_insert_notif = "INSERT INTO notifications (userid,type,message,date,created_date) VALUES ($user_id,'$type','$text_message','$after_one_week','$date'";
		$result_insert_notif = $mysqli->query($sql_insert_notif);
	}
  }
  //route("/getNotifications")
  elseif($_GET['url'] == "getNotifications"){
	  $sql_get_notifications = "SELECT * FROM notifications";
	  $result_get_notifications = $mysqli->query($sql_get_notifications);
	  if($result_get_notifications->num_rows>0){
		  while($row = mysqli_fetch_assoc($result_get_notifications)){
			  $json[] = $row;
		  }
		  echo json_encode($json);
	  }
  }
//-------------------------------------------------------------------------------------------------STATISTICS FOR CARS
  //route("/carsByCategory")
  else if($_GET['url']=="carsByCategory"){
	  $category = $_GET['category'];
	  $sql = 'SELECT COUNT(*) as total_rsvs,cars.category FROM archived_reservations LEFT JOIN cars ON archived_reservations.car_id=cars.id GROUP BY cars.category';
	  $result = $mysqli->query($sql);
	  if($result->num_rows==0){
		echo "0";  
	  }
	  else if($result->num_rows>0){
	   while($row = mysqli_fetch_assoc($result)) {
         if (!is_null($row)){
           $json[] = $row;
         }
       }
		  echo json_encode($json);
	  }
  }
  //route("/carsByCategoryAndMonth")
  else if($_GET['url']=="carsByCategoryAndMonth"){
	  $category = $_GET['category'];
	  $month = $_GET['month'];
	  $sql = "SELECT MONTH(day_start) as month,COUNT(*) as total_rsvs,cars.category FROM archived_reservations LEFT JOIN cars ON archived_reservations.car_id=cars.id GROUP BY MONTH(archived_reservations.day_start)";
	  $result = $mysqli->query($sql);
	  if($result->num_rows==0){
		echo "0";  
	  }
	  else if($result->num_rows>0){
	   while($row = mysqli_fetch_assoc($result)) {
         if (!is_null($row)){
           $json[] = $row;
         }
       }
		  echo json_encode($json);
	  }
  }
  //route("/carsByCategoryAndNationality")
  else if($_GET['url']=="carsByCategoryAndNationality"){
	  $category = $_GET['category'];
	  $sql = 'SELECT COUNT(*) as total_rsvs,clients.nationality FROM archived_reservations LEFT JOIN cars ON reservations.car_id=cars.id LEFT JOIN clients ON reservations.client_id=clients.id GROUP BY clients.nationality,cars.category';
	  
	  $result = $mysqli->query($sql);
	  if($result->num_rows==0){
		echo "0";  
	  }
	  else if($result->num_rows>0){
	   while($row = mysqli_fetch_assoc($result)) {
         if (!is_null($row)){
           $json[] = $row;
         }
       }
		  echo json_encode($json);
	  }

  }
//-------------------------------------------------------------------------------------------------STATISTICS FOR CLIENTS
  //route("/clientsByNationalityAndMonth")
  elseif($_GET['url']=="clientsByNationalityAndMonth"){
	  $sql="SELECT MONTH(day_start) as month,COUNT(*) as total_rsvs,clients.nationality FROM archived_reservations LEFT JOIN clients ON archived_reservations.client_id=clients.id GROUP BY MONTH(archived_reservations.day_start),clients.nationality";
	  $result = $mysqli->query($sql);
	  if($result->num_rows==0){
		echo "0";  
	  }
	  else if($result->num_rows>0){
	   while($row = mysqli_fetch_assoc($result)) {
         if (!is_null($row)){
           $json[] = $row;
         }
       }
		  echo json_encode($json);
	  }
  }
  //route("/clientsByNationality")
  elseif($_GET['url']=="clientsByNationality"){
	  $sql = "SELECT COUNT(*) as total_clients,nationality FROM clients GROUP BY nationality";
	  $result = $mysqli->query($sql);
	  if($result->num_rows==0){
		echo "0";  
	  }
	  else if($result->num_rows>0){
	   while($row = mysqli_fetch_assoc($result)) {
         if (!is_null($row)){
           $json[] = $row;
         }
       }
		  echo json_encode($json);
	  }
  }
//-------------------------------------------------------------------------------------------------STATISTICS FOR RESERVATIONS
  //route("/avgRsvByCarCategory")
  elseif($_GET['url'] == "avgRsvByCarCategory"){
	  $sql = "SELECT cars.category,AVG(DATEDIFF(archived_reservations.day_end, archived_reservations.day_start)) as date_diff_avg FROM archived_reservations LEFT JOIN cars ON archived_reservations.car_id=cars.id GROUP BY cars.category";
	  $result = $mysqli->query($sql);
	  if($result->num_rows==0){
		echo "0";  
	  }
	  else if($result->num_rows>0){
	   while($row = mysqli_fetch_assoc($result)) {
         if (!is_null($row)){
           $json[] = $row;
         }
       }
		  echo json_encode($json);
	  }
  }
  //route("/avgRsvByMonth")
  elseif($_GET['url'] == "avgRsvByMonth"){
	  $sql = "SELECT cars.category,AVG(DATEDIFF(archived_reservations.day_end, archived_reservations.day_start)) as date_diff_avg FROM archived_reservations LEFT JOIN cars ON archived_reservations.car_id=cars.id GROUP BY MONTH(day_start)";
	  $result = $mysqli->query($sql);
	  if($result->num_rows==0){
		echo "0";  
	  }
	  else if($result->num_rows>0){
	   while($row = mysqli_fetch_assoc($result)) {
         if (!is_null($row)){
           $json[] = $row;
         }
       }
		  echo json_encode($json);
	  }
  }

//-------------------------------------------------------------------------------------------------FUNCTIONS
/**
* This algorithm is used to determine if a reservation can be booked for a specific car.
*/
function carIsAvailable($car_id,$day_start,$hour_start,$day_end,$hour_end){
	global $mysqli;
	$carRsvs = "SELECT * FROM reservations WHERE car_id=$car_id";
	$resultCarRsvs = $mysqli->query($carRsvs);
	//store all reservations for the given car into an array.
	$j=0;
	if($resultCarRsvs->num_rows ==0){
		return true;
	}
	else{
		while($row = mysqli_fetch_assoc($resultCarRsvs)) {
    		if (!is_null($row)){
            	$arrRsvs[$j] = $row;
				$j++;
        	}
    	}
	}
	//check if the current car is available for reservation for the given dates.
    for ($i = 0; $i <= count($arrRsvs); $i++) {
		
    	$left_rsv_ds = $arrRsvs[$i]['day_start'];
		$left_rsv_hs = $arrRsvs[$i]['hour_start'];
		
		$right_rsv_ds = $arrRsvs[$i+1]['day_start'];
		$right_rsv_hs = $arrRsvs[$i+1]['hour_start'];
		
		$left_rsv_de = $arrRsvs[$i]['day_end'];
		$left_rsv_he = $arrRsvs[$i]['hour_end'];
		
		$right_rsv_de = $arrRsvs[$i+1]['day_end'];
		$right_rsv_he = $arrRsvs[$i+1]['hour_end'];
		
		//before
		if(strtotime($day_end)<=strtotime($left_rsv_ds)){
			if(strtotime($day_end)==strtotime($left_rsv_ds)){
				if(strtotime($hour_end)<strtotime($right_rsv_he)){
					return true;
				}
				else return false;
			}
			return true;
		}
		//between
		elseif(strtotime($day_start)>=strtotime($left_rsv_de) and strtotime($day_end)<=strtotime($right_rsv_ds)){
			 if(strtotime($day_start)==strtotime($left_rsv_de) or strtotime($day_end)==strtotime($right_rsv_ds)){
				 if(strtotime($hour_start)>strtotime($left_rsv_hs) and strtotime($hour_end)<strtotime($right_rsv_he)){
					return true;
				 }
				 else return false;
			 }
			return true;
		}
		//after
		elseif($right_rsv_ds!="" and (strtotime($day_start)>=strtotime($right_rsv_de))){
			if(strtotime($day_start)==strtotime($right_rsv_de)){
				if(strtotime($hour_start)>strtotime($right_rsv_he)){
					return true;	
				}
				else return false;
			}
			return true;
		}
		elseif($right_rsv_ds == ""){
			if(strtotime($day_start)>=strtotime($left_rsv_de)){
			  if(strtotime($day_start)==strtotime($left_rsv_de)){
				if(strtotime($hour_start)>strtotime($left_rsv_he)){
					return true;	
				}
				else return false;
			  }
			}
			else return false;
			return true;	
		}
	}	
}
/**
* This function is used to notify a specific user of the application about an event (e.g. upcoming reservation,ending reservation
* or an upcoming car service.
*/
function notify($user_id,$type,$text_message,$date,$device_id){
	global $mysqli;
	define( 'API_ACCESS_KEY', 'key' );
	$data = array("to" => "$device_id", "notification" => array( "title" => "$type", "body" => "$text_message","icon" => "icon.png", "click_action" => "http://shareurcodes.com")); 
	$data_string = json_encode($data); 
	echo "The Json Data : ".$data_string; 
	$headers = array ( 'Authorization: key=' . API_ACCESS_KEY, 'Content-Type: application/json' ); 
	$ch = curl_init(); curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' ); 
	curl_setopt( $ch,CURLOPT_POST, true ); 
	curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers ); 
	curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true ); 
	curl_setopt( $ch,CURLOPT_POSTFIELDS, $data_string); 
	$result = curl_exec($ch); 
	curl_close ($ch); 
	echo "<p>&nbsp;</p>"; 
	echo "The Result : ".$result;
	$created_date = date('Y-m-d');
	$sql = "INSERT INTO notifications (userid,type,message,date) VALUES ($user_id,'$type','$message','$date','$created_date')";
	if($mysqli->query($sql)){
		return 200;
	}
	else return 500;
}
/**
* This function is used to sort the available cars for a reservation in descending order based on the number of the total
* reservations within the current month.
*/
function method1($a,$b){
  {
    return $b['monthly_rsvs'] - $a['monthly_rsvs'];
  }
}
/**
* We use this function to make our JSON Pretty for 
* the responses.
*/
function prettyJSON($string){
  $json = json_decode($string);
  return json_encode($json, JSON_PRETTY_PRINT);
}


?>
