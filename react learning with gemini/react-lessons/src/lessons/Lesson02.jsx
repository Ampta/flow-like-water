function Lesson02() {
    const buttonText = "Don't Click Me";
    const buttonColor = "blue";


    return (
        <div>
            <h1>Lesson 2: The Button</h1>
            <button style={{ backgroundColor: buttonColor }}>{buttonText}</button>
            <p>This button doesn't do anything yet!</p>
        </div>
    )
}

export default Lesson02
