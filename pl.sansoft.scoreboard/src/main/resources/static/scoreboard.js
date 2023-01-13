

function getScoreboardData() {

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;
	xhr.overrideMimeType("application/json");
	xhr.onload = () => {
		const data = JSON.parse(xhr.responseText);
		updateView(data);		
	}

	xhr.onerror = () => {
		console.log('Error');
	}
	//xhr.open("GET", "http://localhost:8080/scoreboardData");
	xhr.open("GET", "http://"+window.location.host+"/scoreboardData");

	xhr.send();
}

setInterval(getScoreboardData, 50);
