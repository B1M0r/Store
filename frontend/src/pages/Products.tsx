
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

// This is a redirect component that sends users to the main page
// since our Index page already handles products display
const Products = () => {
  const navigate = useNavigate();
  
  useEffect(() => {
    navigate('/');
  }, [navigate]);
  
  return null;
};

export default Products;
