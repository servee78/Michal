class ScoreboardStream {
    constructor(endpoint) {
        this.endpoint = endpoint;
        this.eventSource = null;
    }

    start() {
        this.eventSource = new EventSource(this.endpoint);

        this.eventSource.addEventListener('message', handleEvent);

        this.eventSource.onerror = (err) => {
            console.log("Error occurred", err);
            updateView(null)
            // this.end();
        }

    }

    // full disconnection, without trying to reconnect
    end() {
        this.eventSoruce.close()
    }



}

const handleEvent = (event) => {
    const data = JSON.parse(event.data)
    // console.log(data)
    updateView(data)

}

const urlEndpoint = 'http://localhost:8080/scoreboardData-sse'
const scoreboardStream = new ScoreboardStream(urlEndpoint)

window.onload = () => {
    scoreboardStream.start()
}

window.onbeforeunload = () => {
    scoreboardStream.end()
}

