function Lesson03() {

    const handleClick = (name) => {
        alert("Hello " + name);
    }

    return (
        <div>
            <h1>Lesson 3: Events</h1>
            <button onClick={() => handleClick("Your Name")}>
                Click Me for an Alert
            </button>
        </div>
    )
}

export default Lesson03
