import "./management-styles/Add.css";

function Add() {
  function handleSubmit(e) {
    // Prevent the browser from reloading the page
    e.preventDefault();

    // Read the form data
    const form = e.target;
    const formData = new FormData(form);

    // You can pass formData as a fetch body directly:
    fetch("/some-api", { method: form.method, body: formData });

    // Or you can work with it as a plain object:
    const formJson = Object.fromEntries(formData.entries());
    console.log(formJson);
  }

  return (
    <div className="add-bg">
      <h1>ADD TASKS</h1>
      <form className="add-container" method="post" onSubmit={handleSubmit}>
        <label className="formLabel">
          Task name: <input name="taskName" className="options" />
        </label>
        <hr className="line" />
        <label className="formLabel">
          Task description: <input name="taskDesc" className="options" />
        </label>
        <hr className="line" />
        <p>
          Status:
          <label className="options">
            <input type="radio" name="myRadio" value="option1" /> Planned
          </label>
          <label className="options">
            <input
              type="radio"
              name="myRadio"
              value="option2"
              defaultChecked={true}
            />{" "}
            In development
          </label>
          <label className="options">
            <input type="radio" name="myRadio" value="option3" /> Testing
          </label>
        </p>
        <hr className="line" />
        <button type="reset">Reset form</button>
        <button type="submit">Submit form</button>
      </form>
    </div>
  );
}

export default Add;
