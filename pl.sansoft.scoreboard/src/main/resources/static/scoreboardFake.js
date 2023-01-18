

// function getScoreboardData() {

// 	var xhr = new XMLHttpRequest();
// 	xhr.withCredentials = true;
// 	xhr.overrideMimeType("application/json");
// 	xhr.onload = () => {
// 		const data = JSON.parse(xhr.responseText);
// 		updateView(data);		
// 	}

// 	xhr.onerror = () => {
// 		console.log('Error');
// 	}
// 	//xhr.open("GET", "http://localhost:8080/scoreboardData");
// 	xhr.open("GET", "http://localhost:5000/scoreboardData");

// 	xhr.send();
// }


// setInterval(getScoreboardData, 5000);

function getScoreboardDataFake() {
	fetch('http://localhost:5000/scoreboardData',
	{
		method: 'GET',
		// mode: 'no-cors',
		headers: {
		'Content-Type': 'application/json'
		}
	})
	.then(response => response.json())
	.then(data => updateView(data))


}

setInterval(getScoreboardDataFake, 500);