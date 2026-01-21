function Lesson05() {
    const shoppingList = ["Apples", "Bananas", "Carrots", "Donuts"];

    return (
        <div>
            <h1>Lesson 5: Lists</h1>
            <h3>Shopping List:</h3>
            <ul>
                {/* We use .map() to turn DATA into UI */}
                {shoppingList.map((item) => (
                    // The 'key' helps React track items. It must be unique.
                    <li key={item}>{item}</li>
                ))}
            </ul>
        </div>
    )
}

export default Lesson05
