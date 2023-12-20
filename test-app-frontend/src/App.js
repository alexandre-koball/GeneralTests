import './App.css';
import Appbar from './components/Appbar'
import PurchaseTransaction from './components/PurchaseTransaction';

const styleFooter = {
  margin: '10px',
  padding: '25px',
  textAlign: 'left',
  fontSize: 12
}

function App() {
  return (
    <div className="App">
      <Appbar />
      <PurchaseTransaction />
      <span style={styleFooter}>Developed by Alexandre Koball (for a test)</span>
    </div>
  );
}

export default App;
